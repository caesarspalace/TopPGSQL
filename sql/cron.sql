SELECT cron.schedule(
'snapshot_pg_stat_history',
'* * * * *',
$$
INSERT INTO pg_stat_activity_history (
snapshot_time, pid, usename, datname, state,
wait_event_type, wait_event, cpu_user_seconds,
cpu_system_seconds,
io_reads_bytes, io_writes_bytes,
shared_blks_hit, shared_blks_read, shared_blks_dirtied,
temp_blks_read, temp_blks_written,
query_id, query
)
SELECT
now(),
a.pid,
a.usename,
a.datname,
a.state,
COALESCE(a.wait_event_type, 'CPU'),
COALESCE(a.wait_event, 'CPU Executing'),
ROUND(CAST(COALESCE(k.plan_user_time + k.exec_user_time, 0) AS numeric), 2),
ROUND(CAST(COALESCE(k.plan_system_time + k.exec_system_time, 0) AS numeric), 2),
-- I/O Física (Kernel via pg_stat_kcache)
COALESCE(k.plan_reads + k.exec_reads, 0) AS io_reads_bytes,
COALESCE(k.plan_writes + k.exec_writes, 0) AS io_writes_bytes,
-- I/O Memoria & Temp (Buffer Cache via pg_stat_statements)
COALESCE(s.shared_blks_hit, 0),
COALESCE(s.shared_blks_read, 0),
COALESCE(s.shared_blks_dirtied, 0),
COALESCE(s.temp_blks_read, 0),
COALESCE(s.temp_blks_written, 0),
a.query_id,
a.query
FROM pg_stat_activity a
LEFT JOIN pg_stat_statements s ON a.query_id = s.queryid
LEFT JOIN pg_stat_kcache_detail k ON s.query = k.query AND a.datname = k.datname
WHERE a.state = 'active'
AND a.pid != pg_backend_pid();
$$
);
