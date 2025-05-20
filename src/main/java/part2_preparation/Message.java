/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package part2_preparation;

/**
 *
 * @author Kintro
 */
public class Message {
    String id;
    int count;
    String recipient;
    String content;
    String hash;

    public Message(String id, int count, String recipient, String content, String hash) {
        this.id = id;
        this.count = count;
        this.recipient = recipient;
        this.content = content;
        this.hash = hash;
    }
}
