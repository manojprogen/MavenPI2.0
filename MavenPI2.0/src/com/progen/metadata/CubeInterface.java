/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.metadata;

/**
 *
 * @author progen
 */
public class CubeInterface {

    public Cube getCube(int roleId) {
        MetadataDAO metadataDAO = new MetadataDAO();
        return metadataDAO.buildCube(roleId);
    }
}
