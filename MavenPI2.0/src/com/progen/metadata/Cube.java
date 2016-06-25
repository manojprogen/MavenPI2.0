/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.metadata;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author progen
 */
public class Cube {

    int subfolderID;
    String subfolderName;
    private int roleID;
    private ArrayList<Dimension> dimensionsList;

    public Cube(int subfolderID, String subfolderName, int roleID) {
        this.subfolderID = subfolderID;
        this.subfolderName = subfolderName;
        this.roleID = roleID;
        dimensionsList = new ArrayList<Dimension>();
    }

    public void addDimension(Dimension dimension) {
        getDimensionsList().add(dimension);
    }

    public static Predicate<Cube> getAccessCubePredicate(final int subFolderId) {
        Predicate<Cube> predicate = new Predicate<Cube>() {

            public boolean apply(Cube input) {
                if (input.subfolderID == subFolderId) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;

    }

    public ArrayList<Dimension> getDimensionsList() {
        return dimensionsList;
    }

    public static Predicate<Cube> getAccessCubeRolePredicate(final int roleID) {
        Predicate<Cube> predicate = new Predicate<Cube>() {

            public boolean apply(Cube input) {
                if (input.roleID == roleID) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;

    }

    public HashMap getWhereClauseMembers(int userId) {
        HashMap memberValues = new HashMap();
        for (Dimension dimension : dimensionsList) {
            ArrayList<DimensionMembers> dimensionMemberses = (ArrayList<DimensionMembers>) dimension.getDimensionMemberslList();
            DimensionMembers dimMember = null;
            for (DimensionMembers dimensionMember : dimensionMemberses) {
                List<MemberSecurity> memberSecuritys = dimensionMember.getMemberSecurityList();
                List<RoleSecurity> roleSecuritys = dimensionMember.getRoleSecurityList();
                if (userId != -1) {
                    for (MemberSecurity security : memberSecuritys) {
                        MemberSecurity memberSecurity = security;
                        if (!memberSecurity.getMemberValues().isEmpty()) {
                            if (userId == memberSecurity.getUserId()) {
                                memberValues.put(Integer.toString(dimensionMember.getMemberId()), Joiner.on(",").join(memberSecurity.getMemberValues()));
                            }

                        }
                    }
                } else {

                    for (RoleSecurity security : roleSecuritys) {
                        RoleSecurity roleSecurity = security;
                        if (!roleSecurity.getMemberValues().isEmpty()) {
                            memberValues.put(Integer.toString(dimensionMember.getMemberId()), Joiner.on(",").join(roleSecurity.getMemberValues()));
                        }

                    }
                }
            }
        }

        return memberValues;
    }
}
