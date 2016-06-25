/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.metadata;

import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author progen
 */
public class DimensionMembers {

    private ArrayList<Integer> elementId;
    private int busTableId;
    private String busTableName;
    private ArrayList<Integer> busTableColumnId;
    private ArrayList<String> busTableColumnName;
    private int memberId;
    private List<MemberSecurity> memberSecurityList;
    private List<RoleSecurity> memberSecurityAtRoleList;

    public DimensionMembers(ArrayList<Integer> elementId, int busTableId, String busTableName, ArrayList<Integer> busTableColumnId, ArrayList<String> busTableColumnName, int memberId) {
        this.elementId = elementId;
        this.busTableId = busTableId;
        this.busTableName = busTableName;
        this.busTableColumnId = busTableColumnId;
        this.busTableColumnName = busTableColumnName;
        this.memberId = memberId;
        memberSecurityList = new ArrayList<MemberSecurity>();
        memberSecurityAtRoleList = new ArrayList<RoleSecurity>();
    }

    public void add(MemberSecurity memberSecurity) {
        getMemberSecurityList().add(memberSecurity);
    }

    public void add(RoleSecurity roleSecurity) {
        getRoleSecurityList().add(roleSecurity);
    }

    public static Predicate<DimensionMembers> getAccessDimensionMemberPredicate(final int memberId) {
        Predicate<DimensionMembers> predicate = new Predicate<DimensionMembers>() {

            public boolean apply(DimensionMembers input) {
                if (input.getMemberId() == memberId) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;

    }

    public ArrayList<Integer> getElementId() {
        return elementId;
    }

    public int getBusTableId() {
        return busTableId;
    }

    public String getBusTableName() {
        return busTableName;
    }

    public ArrayList<Integer> getBusTableColumnId() {
        return busTableColumnId;
    }

    public ArrayList<String> getBusTableColumnName() {
        return busTableColumnName;
    }

    public List<MemberSecurity> getMemberSecurityList() {
        return memberSecurityList;
    }

    public List<RoleSecurity> getRoleSecurityList() {
        return memberSecurityAtRoleList;
    }

    public int getMemberId() {
        return memberId;
    }
}
