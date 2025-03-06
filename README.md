installing MinGW-w64 with MSYS2
=======================================================================
The command you mentioned is for MSYS2, which is a package management system that allows you to install MinGW-w64 (including the latest version 11) easily. The command itself doesn't change based on which version of MinGW-w64 you want - MSYS2 will install the latest available version by default.

Here's how to use MSYS2 to install MinGW-w64:

Step 1: Install MSYS2
=======================================================================
Download MSYS2 installer from https://www.msys2.org/
Run the installer and follow the instructions
When installation completes, the MSYS2 terminal will open automatically

Step 2: Update MSYS2
=======================================================================
pacman -Syu

You may need to close and reopen the MSYS2 terminal after this update.

Step 3: Complete the update
=======================================================================
pacman -Su

Step 4: Install MinGW-w64 Toolchain
=======================================================================
pacman -S --needed base-devel mingw-w64-x86_64-toolchain

This command installs:
The base development tools
The MinGW-w64 64-bit toolchain (including GCC 12.x which is part of MinGW-w64 v11)

Step 5: Add to PATH
=======================================================================
Add the MinGW-w64 bin directory to your system PATH:
Path to add: C:\msys64\mingw64\bin
To add to PATH:
Right-click on "This PC" or "My Computer" and select "Properties"
Click on "Advanced system settings"
Click on "Environment Variables"
Under "System variables", find the "Path" variable and click "Edit"
Click "New" and add C:\msys64\mingw64\bin
Click "OK" on all dialogs

Step 6: Verify Installation
=======================================================================
Open a new Command Prompt window and run:
gcc --version
You should see output indicating GCC 12.x, which is part of MinGW-w64 v11.

Step 7: Build Your DLL
=======================================================================
Navigate to your project directory and run:

[windows]
gcc -shared -o Example.dll src/main/java/com/example/play/cfiles/Example.c

[linux]
gcc -shared -o Example.so src/main/java/com/example/play/cfiles/Example.c

[macos]
gcc -shared -fPIC -o libExample.dylib src/main/java/com/example/play/cfiles/Example.c

This will create the Example.dll file in your project root directory, which can then be used with your Java FFM code.





