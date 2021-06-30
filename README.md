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
  a. To launch manually:
    I. CD into the Container directory:
      - `cd Container`
    II. Build the docker image:
      - `docker build -t priote:1.0 -t priote:latest .`
    III. Run the docker image:
      - `docker run -d -p 3330:3330 priote:latest`
  b. To launch with an automatic script:
    I. CD into the Container directory:
      - `cd Container`
    II. Run the launch script:
      - `./launch.sh`

## License
HQDM and MagmaCore are accessed under the GCHQ Contributor Licence Agreement. These projects were developed by GCHQ and are not my own.
