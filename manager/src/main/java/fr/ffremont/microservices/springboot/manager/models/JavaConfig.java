/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ffremont.microservices.springboot.manager.models;

/**
 *
 * @author florent
 */
class JavaConfig {
    private int minHeap;
    private int maxHeap;

    public int getMinHeap() {
        return minHeap;
    }

    public void setMinHeap(int minHeap) {
        this.minHeap = minHeap;
    }

    public int getMaxHeap() {
        return maxHeap;
    }

    public void setMaxHeap(int maxHeap) {
        this.maxHeap = maxHeap;
    }
}
