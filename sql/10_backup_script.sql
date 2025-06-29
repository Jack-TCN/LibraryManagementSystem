-- 完全备份
BACKUP DATABASE LibraryDB
TO DISK = 'C:\SQLBackup\LibraryDB_Full.bak'
WITH FORMAT, INIT, NAME = 'LibraryDB Full Backup';

-- 差异备份
BACKUP DATABASE LibraryDB
TO DISK = 'C:\SQLBackup\LibraryDB_Diff.bak'
WITH DIFFERENTIAL, FORMAT, INIT, NAME = 'LibraryDB Differential Backup';

-- 日志备份
BACKUP LOG LibraryDB
TO DISK = 'C:\SQLBackup\LibraryDB_Log.trn'
WITH FORMAT, INIT, NAME = 'LibraryDB Log Backup';