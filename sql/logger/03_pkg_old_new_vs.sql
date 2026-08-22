CREATE OR REPLACE 
FUNCTION old_new_vs(t VARCHAR) RETURNS VARCHAR 
AS
$$
DECLARE
	   str VARCHAR(32000) := '';
	   i   integer := 1;
	   cols cursor (tabla VARCHAR) for SELECT * FROM (SELECT attname column_name, typname data_type
					  FROM table_columns
					  WHERE relname = tabla
					  AND typname IN('int2', 'int4', 'int8', 'numeric', 'float4', 'float8', 'money', 'varchar', 'bpchar', 'text', 'timestamp', 'date', 'time', 'bool')) tc
					  LIMIT 50; 
BEGIN
		FOR rcol IN cols(t)
		LOOP
        	   str := str || ' old_vs.a' || trim(to_char(i,'09')) || '_n:=' || '''' || rcol.column_name || ''';';
		  str := str || ' old_vs.a' || trim(to_char(i,'09')) || '_v:=' || 'old.' || rcol.column_name || '::text;';
		  str := str || ' new_vs.a' ||trim(to_char(i,'09')) || '_n:=' || '''' || rcol.column_name || ''';';
		  str := str || ' new_vs.a' || trim(to_char(i,'09')) || '_v:=' || 'new.' || rcol.column_name || '::text;';
		   i := i + 1;
		END LOOP;
		return str;
END;
$$ LANGUAGE PLPGSQL;