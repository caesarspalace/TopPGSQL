DROP VIEW IF EXISTS v_index_advisor;

CREATE VIEW v_index_advisor AS
WITH tablas_criticas AS (
    SELECT 
        schemaname || '.' || relname AS tabla,
        relname AS nombre_tabla,
        seq_scan,
        seq_tup_read,
        idx_scan,
        pg_size_pretty(pg_relation_size(relid)) AS tamanio_tabla,
        ROUND((seq_scan::numeric / NULLIF(seq_scan + idx_scan, 0)) * 100, 2)::float4 AS pct_seq_scan
    FROM pg_stat_user_tables
    WHERE seq_scan > 0 -- Capturar cualquier tabla con escaneos secuenciales
),
consultas_asociadas AS (
    SELECT 
        t.tabla,
        t.seq_scan,
        t.seq_tup_read,
        t.idx_scan,
        t.tamanio_tabla,
        t.pct_seq_scan,
        s.queryid,
        s.query::text AS consulta_sql,
        s.calls AS ejecuciones,
        ROUND(s.mean_exec_time::numeric, 2)::float4 AS tiempo_medio_ms,
        ROW_NUMBER() OVER (PARTITION BY t.tabla ORDER BY s.mean_exec_time DESC) AS rn
    FROM tablas_criticas t
    LEFT JOIN pg_stat_statements s 
        ON s.query ILIKE '%' || t.nombre_tabla || '%'
       AND s.query NOT ILIKE '%pg_stat%'
)
SELECT 
    tabla,
    seq_scan,
    seq_tup_read,
    idx_scan,
    tamanio_tabla,
    pct_seq_scan,
    COALESCE(queryid, 0) AS queryid,
    COALESCE(consulta_sql, 'Sin consulta en pg_stat_statements (filtrado por WHERE atributointeger)') AS consulta_sql,
    COALESCE(ejecuciones, 0) AS ejecuciones,
    COALESCE(tiempo_medio_ms, 0.0) AS tiempo_medio_ms,
    'CREATE INDEX CONCURRENTLY idx_' || replace(split_part(tabla, '.', 2), '-', '_') || '_advisor ON ' || tabla || ' (/* columna_filtro */);'::text AS sugerencia_ddl
FROM consultas_asociadas
WHERE rn = 1 OR rn IS NULL
ORDER BY seq_tup_read DESC;
