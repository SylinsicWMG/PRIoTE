/*
 * Copyright 2021 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package uk.sylinsic.custom;

import java.io.FileWriter;

import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.system.FusekiLogging;
import org.apache.jena.query.Dataset;

import uk.gov.gchq.magmacore.database.MagmaCoreJenaDatabase;

/**
 * Example custom use-case scenario for hosting {@link MagmaCoreJenaDatabase} on a Fuseki server.
 *
 * <p>
 * The Custom class can be used to host in-memory or persistent Magma Core Jena Datasets over
 * HTTP using a Fuseki server.
 * </p>
 * <p>
 * By default, the Fuseki server is configured to run on localhost:3330, however this can be changed
 * by providing a {@code int port} paramaeter to the {@code run()} function.
 * </p>
 * <p>
 * By default, the Fuseki server is also configured to store it's database in the "tdb" directory, 
 * however this cna be changed by providing a {@code string location} parameter to the {@code run()} function.
 * </p>
 * <p>
 * The Fuseki server can host either in-memory Datasets, or connected TDB stores. Datasets can be
 * added to the server using the {@code add(name, dataset)} method. Datasets are hosted at
 * {@code localhost:<port>/<name>}.
 * </p>
 */
public final class Custom {
    
    /**
     * Run the custom Fuseki Server.
     */
    public void run(final String location, final int port) {
        // Create/Connect to persistent database stored at `location`, hosting the server on port `port`
        final MagmaCoreJenaDatabase db = new MagmaCoreJenaDatabase("/usr/src/mymaven/data/".concat(location));
    
        final String graph = "http://www.semanticweb.org/si_units";


        // If db is not already populated, create set of example data objects to store in db.
        db.begin();
        if (db.getDataset().isEmpty()) {
            // Build example data objects Dataset.
            final Dataset objects = CustomDataset.buildDataset();

            db.getDataset().addNamedModel(graph, objects.getDefaultModel());
    
            try {
                final FileWriter output = new FileWriter("/tmp/ontology.owl");
                db.getDataset().getNamedModel(graph).write(output, "RDF/XML");
                output.close();    
            } catch (final Exception e) {
                System.out.println(e);
            }    
            
            db.commit();
        } else {
            db.abort();
        }

        // Build and start Fuseki server.
        final FusekiServer server = FusekiServer
                .create()
                .port(port)
                .add("/".concat(location), db.getDataset(), true)
                .add("/rw/".concat(location), db.getDataset())
                .build();
        FusekiLogging.setLogging();
        server.start();
    }
    
    /**
     * Run the custom Fuseki Server.
     */
    public void run(final String location) {
        // Create/Connect to persistent database stored at `location`, hosting the server on port "3330"
        run(location, 3330);
    }


    /**
     * Run the custom Fuseki Server.
     */
    public void run(final int port) {
        // Create/Connect to persistent database stored at "db", hosting the server on port `port`
        run("db", port);
    }


    /**
     * Run the custom Fuseki Server.
     */
    public void run() {
        // Create/Connect to persistent database stored at "db", hosting the server on port "3330"
        run("db", 3330);
    }

}
