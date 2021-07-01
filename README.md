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
    <ol type="a">
      <li>To launch manually:</li>
        <ol type="1">
          <li>CD into the Container directory:</li>
            - `cd Container`
          <li>Build the docker image:</li>
            - `docker build -t priote:1.0 -t priote:latest .`
          <li>Run the docker image:</li>
            - `docker run -d -p 3330:3330 priote:latest`
        </ol>
      <li>To launch with an automatic script:</li>
        <ol>
          <li>CD into the Container directory:</li>
            - `cd Container`
          <li>Run the launch script:</li>
            - `./launch.sh`
        </ol>
    </ol>

## Hardening

Stripping down of the image to bare minimum, using [strip-image](https://github.com/SylinsicWMG/strip-image)

## License
HQDM and MagmaCore are accessed under the GCHQ Contributor Licence Agreement. These projects were developed by GCHQ and are not my own.
