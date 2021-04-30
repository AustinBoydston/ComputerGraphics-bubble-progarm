Building the application Bubble Pop

1. Using a command prompt/line, move to the directory Build/ which contains all the files for this application. This is done
through the command "cd 'enter directory here'" regardless of platform.
2. Bubble Pop uses gradle so the next step is to simply generate all executables by using the command "gradlew installDist" which will create a build directory with the executables.
3. Follow the directory path of build/install/base/bin/ to find the executables.
4. Open the executable BubblePop.bat and it should open the application and run. This can be done with a file system.
5. When finished, just close out the window and run "gradlew clean" in the Build/ directory to remove the build directory with executables and its files for reduced storage.