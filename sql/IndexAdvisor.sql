drop view v_index_advisor;

CREATE OR REPLACE VIEW v_index_advisor AS
WITH tablas_criticas AS (
    SELECT 
        schemaname || '.' || relname AS tabla,
        relid,
        seq_scan,
        seq_tup_read,
        idx_scan,
        pg_relation_size(relid) AS bytes_tabla,
        pg_size_pretty(pg_relation_size(relid)) AS tamanio_tabla,
        ROUND((seq_scan::numeric / NULLIF(seq_scan + idx_scan, 0)) * 100, 2)::float4 AS pct_seq_scan
    FROM pg_stat_user_tables
    WHERE (seq_scan + idx_scan) > 25 
      AND (seq_scan::numeric / NULLIF(seq_scan + idx_scan, 0)) > 0.10
),
consultas_asociadas AS (
    SELECT 
        s.queryid,
        s.query::text AS consulta_sql,
        s.calls AS ejecuciones,
        ROUND(s.mean_exec_time::numeric, 2)::float4 AS tiempo_medio_ms,
        t.tabla,
        t.seq_scan,
        t.seq_tup_read,
        t.idx_scan,
        t.tamanio_tabla,
        t.pct_seq_scan,
        ROW_NUMBER() OVER (PARTITION BY t.tabla ORDER BY s.mean_exec_time DESC) AS rn
    FROM tablas_criticas t
    JOIN pg_stat_statements s ON s.query ILIKE '%' || split_part(t.tabla, '.', 2) || '%'
    WHERE s.query NOT ILIKE '%pg_stat%'
)
SELECT 
    tabla,
    seq_scan,
    seq_tup_read,
    idx_scan,
    tamanio_tabla,
    pct_seq_scan,
    queryid,
    consulta_sql,
    ejecuciones,
    tiempo_medio_ms,
    'CREATE INDEX CONCURRENTLY idx_' || replace(split_part(tabla, '.', 2), '-', '_') || '_advisor ON ' || tabla || ' (/* columna_filtro */);'::text AS sugerencia_ddl
FROM consultas_asociadas
WHERE rn = 1
ORDER BY seq_tup_read DESC;
