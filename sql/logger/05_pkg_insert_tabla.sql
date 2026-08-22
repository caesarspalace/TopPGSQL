create or replace 
PROCEDURE insert_tabla(t varchar, r varchar, o varchar, un varchar, oldrv a_record, newrv a_record) AS
$$
BEGIN
         if o = 'I' then 
               call insert_logger(t,r,o,'N',inet_client_addr()::text,un, newrv);
        elsif o = 'U' then
             if checkChanges(oldrv, newrv) then 
                	call insert_logger(t,r,o,'O',inet_client_addr()::text,un, oldrv);
                	call insert_logger(t,r,o,'N',inet_client_addr()::text,un, newrv);
              end if;
       elsif o = 'D' then
               call insert_logger(t,r,o,'O',inet_client_addr()::text,un, oldrv);
        end if;
END;
$$ LANGUAGE PLPGSQL;