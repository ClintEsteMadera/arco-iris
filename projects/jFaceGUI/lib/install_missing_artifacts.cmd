mvn install:install-file -DgroupId=org.eclipse.core -DartifactId=commands -Dversion=3.3.0-I20070605-0010 -Dpackaging=jar -Dfile=swt_jface/commands.jar
mvn install:install-file -DgroupId=org.eclipse.equinox -DartifactId=org.eclipse.equinox.common -Dversion=3.6.0.v20100503 -Dpackaging=jar -Dfile=swt_jface/equinox_common.jar
mvn install:install-file -DgroupId=org.eclipse -DartifactId=swt -Dversion=3.3.0-v3346 -Dpackaging=jar -Dfile=swt_jface/swt.jar
mvn install:install-file -DgroupId=org.eclipse -DartifactId=jface -Dversion=3.3.0-I20070606-0010 -Dpackaging=jar -Dfile=swt_jface/jface.jar
mvn install:install-file -DgroupId=org.eclipse.osgi -DartifactId=org.eclipse.osgi -Dversion=3.6.0.v20100517 -Dpackaging=jar -Dfile=swt_jface/osgi.jar
mvn install:install-file -DgroupId=org.eclipse.swt.win32.win32 -DartifactId=x86 -Dversion=3.3.0-v3346 -Dpackaging=jar -Dfile=swt_jface/native-lib-win32.jar
mvn install:install-file -DgroupId=org.eclipse -DartifactId=swt-win32-win32-x86_64 -Dversion=3.5.2 -Dpackaging=jar -Dfile=swt_jface/native-lib-win64.jar
mvn install:install-file -DgroupId=org.eclipse.swt.gtk.linux -DartifactId=x86 -Dversion=3.3.0-v3346 -Dpackaging=jar -Dfile=swt_jface/native-lib-linux32.jar
mvn install:install-file -DgroupId=org.eclipse.swt.gtk.linux -DartifactId=x86_64 -Dversion=3.3.0-v3346 -Dpackaging=jar -Dfile=swt_jface/native-lib-linux64.jar
mvn install:install-file -DgroupId=org.eclipse.ui -DartifactId=forms -Dversion=3.2.0.v20060602 -Dpackaging=jar -Dfile=swt_jface/forms.jar
mvn install:install-file -DgroupId=org.vafada -DartifactId=swtcalendar -Dversion=0.5 -Dpackaging=jar -Dfile=swtcalendar.jar

