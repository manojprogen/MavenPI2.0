/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.db;

import java.io.Serializable;

/**
 *
 * @author arun
 */
public class QueryResultValObj implements Serializable {

    private static final long serialVersionUID = 56798900426588179L;
    private PbReturnObject retObj;

    public QueryResultValObj(PbReturnObject retObj) {
        this.retObj = retObj;
    }

    public PbReturnObject getReturnObject() {
        return this.retObj;
    }
}
