@echo off
set STENDHAL_VERSION=1.49.5

if exist "stendhal-server-%STENDHAL_VERSION%.jar" (
	set LOCALCLASSPATH=.\stendhal-server-%STENDHAL_VERSION%.jar;.\libs\*
) else if exist "build\lib\stendhal-server-%STENDHAL_VERSION%.jar" (
	set LOCALCLASSPATH=.\build\lib\stendhal-server-%STENDHAL_VERSION%.jar;.\build\lib\stendhal-server-maps-%STENDHAL_VERSION%.jar;.\build\lib\stendhal-server-xmlconf-%STENDHAL_VERSION%.jar;.\libs\*
) else (
	set LOCALCLASSPATH=.;.\build\build_server;.\build\build_server_maps;.\build\build_server_script;.\build\build_server_xmlconf;.\libs\*;
)

java -Xmx512m -cp "%LOCALCLASSPATH%" games.stendhal.server.StendhalServer -c server.ini %*

:end
@pause
