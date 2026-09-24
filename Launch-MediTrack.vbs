Set WshShell = CreateObject("WScript.Shell")
strDir = CreateObject("Scripting.FileSystemObject").GetParentFolderName(WScript.ScriptFullName)
WshShell.CurrentDirectory = strDir
WshShell.Run "javaw -cp ""out;lib\mysql-connector-j.jar;MediTrack.jar"" Main", 1, false
