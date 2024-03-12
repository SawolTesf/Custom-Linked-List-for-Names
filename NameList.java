import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;
import java.util.HashMap;

class LinkedList{
    private Node head;
    private int size;
    private HashMap<String, Integer> letters;

    LinkedList(){
        this.head = null;
        this.size = 0;
        this.letters = new HashMap<String, Integer>(); // To keep track of if we have a letter in the linkedList or not
    }

    public void addName(String name){
        if(name.length() < 2){
            System.out.println("Name must be at least 2 characters");
            return;
        }

        String letter = String.valueOf(name.charAt(0)).toUpperCase();
        if(head == null){
            head = new Node(letter, new Node(name, null));
            size+=2;
            letters.put(letter, letters.getOrDefault(letter, 0)+1);
            return;
        }
        int amount = letters.getOrDefault(letter, 0);

        // Need to add 2 new nodes. Letter and name
        if(amount == 0){
            Node current = head;
            Node prev = null;
            Node wordNode = new Node(name, null);
            Node letterNode = new Node(letter, wordNode);

            // Traverse to insertion point
            while(current != null){
                // If letter Node and letter Node is greater alphabetically greater
                if(current.value.length() == 1 && current.value.compareToIgnoreCase(letter) > 0){
                    break;
                }
                prev = current;
                current = current.next;
            }
            if(prev == null){
                wordNode.next = current;
                head = letterNode;
            }
            else{
                wordNode.next = current;
                prev.next = letterNode;                
            }
            size+=2;
            letters.put(letter, letters.getOrDefault(letter, 0) + 1);
        }
        else{
            Node current = head;
            Node prev = null;

            // Get the pointers in the area with same letter
            while(current != null && letter.compareToIgnoreCase(String.valueOf(current.value.charAt(0))) != 0){
                prev = current;
                current = current.next;
            }

            // Now get current pointer to a name that is greater than param name so that we can place param name in between prev pointer and current pointer
            while(current != null && letter.compareToIgnoreCase(String.valueOf(current.value.charAt(0))) == 0){
                
                // If current node is not a letter node and the the current node name initialze is the same as param name and param name is smaller than current name, break
                if(current.value.length() > 1 && letter.compareToIgnoreCase(String.valueOf(current.value.charAt(0))) == 0 && current.value.compareToIgnoreCase(name) > 0){
                    break;
                }
                prev = current;
                current = current.next;
            }
            // If adding to the end of the linked list
            if(current == null){
                prev.next = new Node(name, null);
            }
            else{
                Node newNode = new Node(name, current);
                prev.next = newNode;
            }
            size++;
            letters.put(letter, letters.get(letter) + 1);
        }
    }
    public void removeName(String name){
        if(head == null){
            System.out.println("List is empty");
            return;
        }
    
        Node current = head;
        Node previous = null;
    
        while(current != null){
            if(current.value.equals(name)){
                break;
            }
            previous = current;
            current = current.next;
        }
    
        if(current == null){
            System.out.println("Name not found");
            return;
        }
    
        if(previous == null){
            head = current.next;
        } 
        else {
            previous.next = current.next;
        }
    
        size--;
        String letter = String.valueOf(name.charAt(0)).toUpperCase();
        int count = letters.get(letter);
        if(count == 1){
            removeLetter(letter);
        } 
        else {
            letters.put(letter, count - 1);
        }
    }
    public void removeLetter(String letter){
        if(head == null){
            System.out.println("List is empty");
            return;
        }
    
        Node current = head;
        Node previous = null;
    
        while(current != null){
            if(current.value.equals(letter)){
                break;
            }
            previous = current;
            current = current.next;
        }
    
        if(current == null){
            System.out.println("Letter not found");
            return;
        }
    
        // Skip all nodes associated with the letter
        while(current != null && current.value.startsWith(letter)){
            current = current.next;
            size--;
        }
    
        if(previous == null){
            head = current;
        } 
        else {
            previous.next = current;
        }
    
        letters.remove(letter);
    }
    public boolean findName(String name){
        if(head == null || letters.getOrDefault(String.valueOf(name.charAt(0)), 0) == 0){
            return false;
        }
        Node current = head;
        while(current != null){
            if(current.value.equalsIgnoreCase(name)){
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        Node current = head;
        while(current != null){
            if(current.value.length() < 2){
                sb.append(current.value + "\n");
            }
            else{
                sb.append("\t" + current.value + "\n");
            }
            current = current.next; // Added missing line to move to the next node
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString(); // Replaced the line with the fix
    }
    
    private static class Node{
        private String value;
        private Node next;

        Node(){
            this.value = "";
            this.next = null;
        }
        Node(String value, Node next){
            this.value = value;
            this.next = next;
        }
    }

}
class NameList{
    public static void main(String args[]){

        /*   if(args.length < 1){
            System.out.println("Usage: java NameList <filename.txt>");
            return;
        }
        */

        LinkedList LL = new LinkedList();
        Scanner fileScanner = null;
        Scanner lineScanner = null;
    
        fileScanner = new Scanner(System.in);

        while(fileScanner.hasNextLine()){
            String line = fileScanner.nextLine().trim();
            if(line.isEmpty()){
                continue;
            }
            String name = null;
            lineScanner = new Scanner(line);
            String command = lineScanner.next();

            if(!command.equals("S")){
                name = lineScanner.next();
            }
            switch(command){
                case "A":
                    LL.addName(name);
                    break;
                case "R":
                    if(name.length() > 1){
                        LL.removeName(name);
                    } 
                    else {
                        LL.removeLetter(name);
                    }
                    break;
                case "C":
                    boolean found = LL.findName(name);
                    System.out.println(found ? "Found" : "Not Found");
                    break;
                case "S":
                    System.out.println(LL.toString());
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }

        fileScanner.close();
    }
}