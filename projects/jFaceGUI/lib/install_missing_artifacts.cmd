cmd /c mvn install:install-file -DgroupId=org.eclipse.core -DartifactId=core_commands -Dversion=6.6.6 -Dpackaging=jar -Dfile=swt_jface/core_commands.jar
cmd /c mvn install:install-file -DgroupId=org.eclipse.core -DartifactId=equinox_common -Dversion=6.6.6 -Dpackaging=jar -Dfile=swt_jface/equinox_common.jar
cmd /c mvn install:install-file -DgroupId=org.eclipse -DartifactId=swt -Dversion=6.6.6 -Dpackaging=jar -Dfile=swt_jface/swt.jar
cmd /c mvn install:install-file -DgroupId=org.eclipse -DartifactId=jface -Dversion=6.6.6 -Dpackaging=jar -Dfile=swt_jface/jface.jar
cmd /c mvn install:install-file -DgroupId=org.eclipse.swt.win32.win32 -DartifactId=x86 -Dversion=6.6.6 -Dpackaging=jar -Dfile=swt_jface/native-lib-win32.jar
cmd /c mvn install:install-file -DgroupId=org.eclipse -DartifactId=osgi -Dversion=6.6.6 -Dpackaging=jar -Dfile=swt_jface/osgi.jar

cmd /c mvn install:install-file -DgroupId=javax.ejb -DartifactId=ejb -Dversion=2.1 -Dpackaging=jar -Dfile=ejb-2.1.jar
cmd /c mvn install:install-file -DgroupId=org.eclipse.ui -DartifactId=forms -Dversion=3.2.0.v20060602 -Dpackaging=jar -Dfile=forms.jar

cmd /c mvn install:install-file -DgroupId=sba.cajval -DartifactId=framework_app -Dversion=1.0 -Dpackaging=jar -Dfile=framework_app-1.0.jar
cmd /c mvn install:install-file -DgroupId=sba.cajval -DartifactId=sba-security -Dversion=0.0.1 -Dpackaging=jar -Dfile=sba-security-0.0.1.jar
cmd /c mvn install:install-file -DgroupId=sba.cajval -DartifactId=sba-session -Dversion=1.0.2 -Dpackaging=jar -Dfile=sba-session-1.0.2.jar
cmd /c mvn install:install-file -DgroupId=org.vafada -DartifactId=swtcalendar -Dversion=0.5 -Dpackaging=jar -Dfile=swtcalendar.jar
cmd /c mvn install:install-file -DgroupId=sba.cajval -DartifactId=usuarios-stubs -Dversion=1.0 -Dpackaging=jar -Dfile=usuarios-stubs.jar