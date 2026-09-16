CREATE OR REPLACE FUNCTION explain(sql text) RETURNS text
AS $$
DECLARE
   c text;
   num_params int := 0;
   stmt_types text := '';
   stmt_vals text := '';
   stmt_name text;
   i int;
BEGIN
  -- 1. Contar la cantidad de signos '$' en la consulta SQL
  SELECT regexp_count(sql, '\$') INTO num_params;

  IF num_params > 0 THEN
    -- Nombre único usando timestamp y microsegundos
    stmt_name := '"stmt_' || clock_timestamp()::text || '"';

    -- 2. Construir la lista de tipos ('unknown, unknown...') y valores ('null, null...')
    FOR i IN 1..num_params LOOP
      IF i = 1 THEN
        stmt_types := 'unknown';
        stmt_vals  := 'null';
      ELSE
        stmt_types := stmt_types || ', unknown';
        stmt_vals  := stmt_vals  || ', null';
      END IF;
    END LOOP;

    -- 3. Preparar la sentencia con los N tipos detectados
    EXECUTE 'PREPARE ' || stmt_name || ' (' || stmt_types || ') AS ' || sql;
    
    -- 4. Forzar el plan genérico
    EXECUTE 'SET plan_cache_mode = force_generic_plan';

    -- 5. Ejecutar EXPLAIN pasando los N valores nulos
    EXECUTE 'EXPLAIN (FORMAT JSON) EXECUTE ' || stmt_name || '(' || stmt_vals || ')' INTO c;

    -- 6. Limpiar la sentencia preparada del cache de la sesión
    EXECUTE 'DEALLOCATE ' || stmt_name;

  ELSE
    -- Consulta sin parámetros parametrizados
    EXECUTE 'EXPLAIN (FORMAT JSON) ' || sql INTO c;
  END IF;

  RETURN c;
END;
$$ LANGUAGE plpgsql;
