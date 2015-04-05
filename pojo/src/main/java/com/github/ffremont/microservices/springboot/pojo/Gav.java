/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ffremont.microservices.springboot.pojo;

/**
 *
 * @author florent
 */
public class Gav {

    private String groupId;
    private String artifactId;
    private String version;
    private String packaging;
    private String classifier;

    public Gav() {

    }

    public Gav(String groupId, String artifactId, String packaging, String classifier, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.packaging = packaging;
        this.classifier = classifier;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    /**
     * groupId:artifactId:packaging:classifier:version
     *
     * @return
     */
    @Override
    public String toString() {
        if (this.classifier == null) {
            return this.groupId + ":" + this.artifactId + ":" + this.packaging + ":" + this.version;
        } else {
            return this.groupId + ":" + this.artifactId + ":" + this.packaging + ":" + this.classifier + ":" + this.version;
        }
    }

}
