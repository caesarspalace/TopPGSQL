create or replace 
PROCEDURE create_trg_insert(t VARCHAR) AS
$$
DECLARE
	  str VARCHAR(32000) := '';
	  proc varchar(30) := 'pr_' || t;
	  oldnewvalue varchar(4000) := '';
BEGIN
       BEGIn
           execute 'drop trigger ' || 'tr_' || t || ' on ' || t;
	EXCEPTION WHEN OTHERS THEN 
	      NULL;
	END;
       BEGIn
	  execute 'drop trigger ' || 'tr_ud' || t || ' on ' || t;
	EXCEPTION WHEN OTHERS THEN 
	      NULL;
	END;
	  str := str || 'create or replace function ' || proc || '() returns trigger AS $' || proc || '$';
  	  str := str || ' DECLARE ' ;
	  str := str || ' new_vs a_record;';
	  str := str || ' old_vs a_record;';
	  str := str || ' tv        varchar(1);';
	  str := str || ' idrow  varchar(100);';
	  str := str || ' BEGIN ' ;
	  oldnewvalue := old_new_vs(t);
	  str := str || oldnewvalue;
	  str := str  ||  ' IF (TG_OP = ''DELETE'') THEN ';
	  str := str || '  tv := ''D''; idrow := old.ctid::text;';
	  str := str  ||  ' ELSIF (TG_OP = ''UPDATE'') THEN ';
	  str := str || '  tv := ''U''; idrow := old.ctid::text;';
	  str := str  ||  ' ELSIF (TG_OP = ''INSERT'') THEN ';
	  str := str || '  tv := ''I''; idrow := new.ctid::text;';
	  str := str || ' END IF;';
	  str := str || ' call insert_tabla(''' || t || ''',idrow,tv, session_user::text, old_vs, new_vs);';
	  str := str  ||  ' IF (TG_OP = ''DELETE'') THEN ';
	  str := str || '   return old;';
	  str := str  ||  ' ELSIF (TG_OP = ''UPDATE'') THEN ';
	  str := str || '   return new;';
	  str := str  ||  ' ELSIF (TG_OP = ''INSERT'') THEN ';
	  str := str || '  return new;';
	  str := str || ' END IF;';
	  str := str || ' END; ';
	  str := str || ' $' || proc || '$ LANGUAGE PLPGSQL; ';
	  execute str;
	  str := '';
	  str := 'CREATE TRIGGER ' || 'tr_' || t;
	  str := str || ' AFTER INSERT ON ' || t;
	  str := str || ' FOR EACH ROW ';
	  str := str || ' EXECUTE PROCEDURE ' || proc || '();'; 
	  execute str;
	  str := '';
  	  str := 'CREATE TRIGGER ' || 'tr_ud' || t;
	  str := str || ' BEFORE UPDATE OR DELETE ON ' || t;
	  str := str || ' FOR EACH ROW ';
	  str := str || ' EXECUTE PROCEDURE ' || proc || '();'; 
	  execute str;
END;
$$ LANGUAGE PLPGSQL;
