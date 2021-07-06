# PRIoTE

## About 

A project that containerises GCHQ's HQDM and Magma Core

- [HQDM](https://github.com/gchq/HQDM.git)
- [Magma Core](https://github.com/gchq/MagmaCore.git)

---

## Implementation

To create the container for this environment, we first determine requirements of the two projects, [HQDM](https://github.com/gchq/HQDM.git) and [Magma Core](https://github.com/gchq/MagmaCore.git). All that is required is [Apache Maven](https://maven.apache.org/), and [Java Development Kit version 15](https://openjdk.java.net/projects/jdk/15/).
Luckily for us, there is already a docker image for this which we can use: `maven:maven:3.8.1-openjdk-15`, so we use this image with the line `FROM maven:maven:3.8.1-openjdk-15` in our [Dockerfile](Container/Dockerfile)

Now that we have a base image available for usage, we must add in the files for [HQDM](https://github.com/gchq/HQDM.git) and [Magma Core](https://github.com/gchq/MagmaCore.git). To do this, we clone each repository into a new subfolder `files`. We then add the line `COPY files/ /usr/rsc/mymaven` to our [Dockerfile](Container/Dockerfile) such that our files are copied into our new image. We declare our `WORKDIR` as `/usr/src/mymaven` such that we are working from this directory upon boot.

Next, environmental variables must be declared in our [Dockerfile](Container/Dockerfile) to ensure our stripped image is able to successfully locate the appropriate programs and files for successful running.

Finally, we must write two scripts, one to be ran on building of the image, and one to be ran on running of the container. The first of these, [build.sh](Container/files/build.sh), compiles the two projects in the work directory using maven. Whilst the latter, [entrypoint.sh](Container/files/entrypoint.sh), creates some symbolic links for ease of use once the image has been stripped, and then simply runs the [Magma Core](https://github.com/gchq/MagmaCore.git) application using maven. To add these scripts appropriately, we use the `RUN` and `ENTRYPOINT` keywords respectively.

---

## Installation

Prior to any installation, ensure that your repository listings are updated and software packages are up-to-date.

---

### Docker

#### Linux:

1. Download the docker installation script using one of the following methods:
    - `curl -fsSL https://get.docker.com/ -o install_docker.sh`
    - `wget https://get.docker.com/ -o install_docker.sh`
2. Enable execute permissions on the installation script:
    - `chmod +x install_docker.sh`
3. Run the installation script:
    - `./install_docker.sh`

#### Mac OSX

1. Download the Docker Desktop installation program from [Docker Hub](https://hub.docker.com/editions/community/docker-ce-desktop-mac), selecting the appropriate architecture for your device, direct links:
    - [Intel chip](https://desktop.docker.com/mac/stable/amd64/Docker.dmg)
    - [Apple chip](https://desktop.docker.com/mac/stable/arm64/Docker.dmg)
2. Open the downloaded `.dmg` file, and once loaded, drag the icon into the `Applications folder`:
    ![Drag and drop image](https://docs.docker.com/docker-for-mac/images/docker-app-drag.png)
    Source: https://docs.docker.com/docker-for-mac/images/docker-app-drag.png

--- 

### Git

- Install using `apt`:
    - `apt install git`
- Install using `pacman`:
    - `pacman -S git`
- Install using `yum`:
    - `yum install git`

---

### Container

1. Clone this repository:
    - `git clone ssh://git@github.com/SylinsicWMG/priote.git --recurse-submodules`
2. CD into the new folder for this repository:
    - `cd priote`
3. Launching the container can be done either manually or automatically:
    - To launch manually:
        1. CD into the Container directory:
            - `cd Container`
        2. Build the docker image:
            - `docker build -t priote:1.0 -t priote:latest .`
        3. Strip the image down to all that is required:
            - ```
              scripts/strip-image \
                -i priote:latest \
                -t priote_stripped:latest \
                -d Dockerfile \
                -x 3330 \
                -f /etc/group \
                -f /etc/nsswitch.conf \
                -f /etc/passwd \
                -f /root/.m2/ \
                -f /usr/bin/basename \
                -f /usr/bin/bash \
                -f /usr/bin/cd \
                -f /usr/bin/coreutils \
                -f /usr/bin/dirname \
                -f /usr/bin/echo \
                -f /usr/bin/expr \
                -f /usr/bin/ln \
                -f /usr/bin/ls \
                -f /usr/bin/pwd \
                -f /usr/bin/readlink \
                -f /usr/bin/sh \
                -f /usr/bin/test \
                -f /usr/bin/tr \
                -f /usr/bin/uname \
                -f /usr/java/openjdk-15/ \
                -f /usr/lib64/libc.so.6 \
                -f /usr/lib64/libm.so.6 \
                -f '/usr/lib64/libnss*' \
                -f /usr/lib64/libz.so.1 \
                -f /usr/share/maven/ \
                -f /usr/src/mymaven/```
        4. Compile and insert the SELinux policy and port for the container:
            - `cd policies`
            - `sudo make -f /usr/share/selinux/devel/Makefile priote.pp`
            - `sudo semodule -i priote.pp`
            - `sudo semanage port -a -t priote_port_in_t -p tcp 3330`
        4. Run the stripped image:
            - `docker run -d -p 3330:3330 --cap-drop=all --security-opt seccomp=policies/seccomp.json --security-opt label:type:priote_t priote_stripped:latest`
    - To launch with an automatic script:
        1. CD into the Container directory:
            - `cd Container`
        2. Run the launch script:</li>
            - `./launch.sh`

---

## Hardening

---

### Capabilities

To limit potential exploit points of the container against the host, all capabilities of the container are dropped as they are unnecessary and not required for successful operation.

---

### Seccomp

Seccomp is utilised to limit the system calls available for usage by the container. 

An `strace` binary along with the required libraries was copied into the container, and then utilised to obtain a list of used system calls within the execution process, i.e. `strace -f ./entrypoint.sh -o calls`. This list of calls was then formatted and filtered to obtain the raw list of system call names before being inserted into the [Seccomp profile](Container/policies/seccomp.json).

---

### SELinux

SELinux policies have been implemented to limit the container's abilities to the task it is designed to do. Only specified system calls are able to be made on specified labels.
 For docker to function with SELinux, we must first install SELinux on the host machine and enable it:
    - `sudo apt install policycoreutils selinux-utils selinux-basics`
    - `sudo selinux-activate`
    - `reboot`

Following the installation of SELinux on the host system, we must modify Docker's service file to enable SELinux with the `--selinux-enabled` switch. The docker service file is system dependent, however it can be found with:
    - `sudo find / -name "docker.service"`
    
A common place for this file to reside is `/usr/lib/systemd/system/docker.service`.
Once this file has been located, we must open this file in our chosen editor, and append `--selinux-enabled` to the line responsible for the starting/execution of the service, usually `ExecStart`.

Following the enabling of SELinux in docker, we must restart the docker service with the reloaded service file:
1. sudo systemctl daemon-reload
2. systemctl restart docker

To create the SELinux profile, a blank policy is created in `permissive` mode. [This policy](Container/policies/priote.te) was compiled and then utilised as a security option upon launch of the container. This causes notifications to be thrown in `/var/log/audit/audit.log`. However, this isn't particularly easy on the eye to read, so instead, we use the `ausearch` command line tool to identify the latest errors, then pipe these through to the `audit2allow` command which prints them in a form ready for direct insertion into the policy. This command sequence has been created as the script ['scripts/check_auditlog.sh'](Container/scripts/check_auditlog.sh).

Once notifications are thrown in the audit log, we insert the required policy lines into our policy file and increase our policy version by 0.1, then re-compile and insert it. At each stage, we also attempt to access as many of the functions of the container as possible, i.e. processing a query on the interface. We then relaunch the container with the new policy and perform this process repeatedly until we are no longer thrown any new notifications. Once we reach this point, we remove the `permissive` line in the policy file and try once more. Ideally we should have a working system at this point, however, there are some `dontaudit` rules which are blocking our program from successfully running. We run `sudo semodule -DB` to rebuild the policy with `dontaudit` policies disable. Restart the container and then check the audit log again. We will see some new entries, probably for the `read` or `write` system calls. We add these in and now have our fully functioning profile for SELinux. Once it is fully functioning, we set our policy version to 1.0.

---

### Stripping

The container has been stripped such that only files which are required for operation are remaining within the container.
Stripping down of the image is performed using [strip-image](https://github.com/SylinsicWMG/strip-image), with our fully usage in our [launch script](Container/launch.sh).

To strip the container, we first look for key files we will need in our initial files, i.e. bash (as seen in the shebang of our [entrypoint script](Container/files/entrypoint.sh)), as well as `readlink`, `dirname`, `basename` etc. All the files/programs we see to are used, we add in to the stripping command of our launch script.We also add any other files that we know are required, such as the maven repository. In addition to files, we also add some additional command line arguments to the stripping command, such as the docker tag of the original image, the new image, the Dockerfile to use, published ports etc.

Following this, we run our launch script, and wait for the machine to come online, we check the logs using `docker logs $(docker ps -ql)`, to see what errors are thrown (if any). If there are errors which suggest a file or library we must include, we add them to the stripping command, before relaunching the container and checking the logs again. If an error is thrown although not a useful one, i.e. one that doesn't suggest a file, then we use a combination of `ldd`, `LD_DEBUG=libs` and `strace` to identify any files that the program is attempting to use but can't find. Once there are no longer any errors and the container boots successfully, our stripping command is complete.

---

## License

HQDM and MagmaCore are accessed under the GCHQ Contributor Licence Agreement. These projects were developed by GCHQ and are not my own.
