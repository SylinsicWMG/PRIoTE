# PRIoTE

## About 

A project to attempt to containerise GCHQ's HQDM and MagmaCore

- [HQDM on github](https://github.com/gchq/HQDM.git)
- [MagmaCore on github](https://github.com/gchq/MagmaCore.git)

## Installation

1. Clone this repository:
    - `git clone https://github.com/SylinsicWMG/PRIoTE.git`
2. CD into the new folder for this repository:
    - `cd PRIoTE`
3. Launching the container can be done either manually (a) or automatically (b):
    1. To launch manually:
        1. CD into the Container directory:
            - `cd Container`
        2. Build the docker image:
            - `docker build -t priote:1.0 -t priote:latest .`
        3. Run the docker image:
            - `docker run -d -p 3330:3330 priote:latest`
    2. To launch with an automatic script:
        1. CD into the Container directory:
            - `cd Container`
        2. Run the launch script:</li>
            - `./launch.sh`

## Hardening

### Capabilities

To limit potential exploit points of the container against the host, all capabilities of the container are dropped as they are unnecessary and not required for successful operation.

### Seccomp

Seccomp is utilised to limit the system calls available to the container. Restricting potential exploit points via system calls.

### SELinux

SELinux policies have been implemented to limit the container to only what is required. For docker to function with SELinux, we must first install SELinux on the host machine and enable it:
    - `sudo apt install policycoreutils selinux-utils selinux-basics`
    - `sudo selinux-activate`
    - `reboot`

Following the installation of SELinux on the host system, we must modify Docker's service file to enable SELinux with the `--selinux-enabled` switch. The docker service file is system dependent, however it can be found with:
    - `sudo find / -name "docker.service"`
    
A common place for it to reside is `/usr/lib/systemd/system/docker.service`.
Once this file has been located, one must open this file in their chosen editor, and append `--selinux-enabled` to the line responsible for the starting/execution of the service, usually `ExecStart`.

Following the enabling of SELinux in docker, we must restart the docker service with the reloaded service file:
1. sudo systemctl daemon-reload
2. systemctl restart docker

### Stripping

The container has been stripped such that only files which are required for operation are remaining within the container.
Stripping down of the image is performed using [strip-image](https://github.com/SylinsicWMG/strip-image)


## License

HQDM and MagmaCore are accessed under the GCHQ Contributor Licence Agreement. These projects were developed by GCHQ and are not my own.
