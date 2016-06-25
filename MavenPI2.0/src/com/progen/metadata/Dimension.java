/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.metadata;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author progen
 */
public class Dimension {

    private int dimensinId;
    private String dimensionName;
    private List<DimensionMembers> dimensionMemberslList;

    public Dimension(int dimensinId, String dimensionName) {
        this.dimensinId = dimensinId;
        this.dimensionName = dimensionName;
        dimensionMemberslList = new ArrayList<DimensionMembers>();
    }

    public void addDimensionMember(DimensionMembers dimensionMembers) {
        getDimensionMemberslList().add(dimensionMembers);
    }

    public List<DimensionMembers> getDimensionMemberslList() {
        return dimensionMemberslList;
    }
}
