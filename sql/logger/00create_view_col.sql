-- DEBE SER EJECUTADO COMO SYS y DAR GRANTEO AL USUARIO.
create view table_columns as
select relname, attname,  typname
from pg_attribute, pg_type d , pg_class c, pg_authid u
where  d.oid = atttypid 
      and c.oid = attrelid 
      and u.oid = relowner 
      and relkind in ('r','v') 
      and attname not in ('tableoid','cmax','xmax','cmin','xmin','ctid')
order by relname,attnum;
