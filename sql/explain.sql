create or replace function explain(sql text) returns text
as 
$$
declare
   c text;
   pesosCount text[];
   z1 text := 'PREPARE ';
   stmt text := 'stmt(';
   stmt1 text := 'stmt(';
   z text;
   i int := 0;
   fecha text;
begin
  select regexp_matches(sql,'[\$]','g') into pesosCount;
  select now()::text into fecha;
  stmt := '"' || fecha || '"' || '(';
  stmt1 := '"' || fecha || '"' || '(';
  if pesosCount is not null THEN
  foreach z in array pesosCount
  loop 
    if i = 0 THEN 
        stmt := stmt || 'unknown';
        stmt1 := stmt1 || 'null';
    else 
        stmt := stmt || ',unknown';
        stmt1 := stmt1 || ',unknown';
    end if;
    i := i + 1;
  end loop;
  execute z1 || stmt || ') AS ' || sql;
  execute 'set plan_cache_mode = force_generic_plan';
  execute 'EXPLAIN (FORMAT JSON) EXECUTE' || stmt1 || ')' into c;
  else 
  execute 'EXPLAIN (FORMAT JSON) ' || sql into c;
  end if;
  return c;
end;
$$ LANGUAGE plpgsql;
