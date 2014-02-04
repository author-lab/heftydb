/*
 * Copyright (c) 2014. Jordan Williams
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jordanwilliams.heftydb.test.integration;

import com.jordanwilliams.heftydb.data.Tuple;
import com.jordanwilliams.heftydb.db.HeftyDB;
import com.jordanwilliams.heftydb.db.Snapshot;
import com.jordanwilliams.heftydb.state.Config;
import com.jordanwilliams.heftydb.test.base.ParameterizedIntegrationTest;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

public class ReadWriteTest extends ParameterizedIntegrationTest {

    public ReadWriteTest(List<Tuple> tuples, Config config) throws IOException {
        super(tuples, config);
        writeRecords();
    }

    @Test
    public void basicIteratorTest() throws Exception {
        db = HeftyDB.open(config);

        Iterator<Tuple> dbIterator = db.ascendingIterator(Snapshot.MAX);

        while (dbIterator.hasNext()){
            dbIterator.next().key().snapshotId();
        }

        db.close();
    }

    @Test
    public void readWriteTest() throws Exception {
        db = HeftyDB.open(config);

        for (Tuple tuple : tuples){
            ByteBuffer key = tuple.key().data();
            Tuple read = db.get(key);
            System.out.println(read);
        }

        db.close();
    }

    private void writeRecords() throws IOException {
        for (Tuple tuple : tuples){
            db.put(tuple.key().data(), tuple.value().data());
        }

        db.close();
    }
}
