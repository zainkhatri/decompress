import java.io.*;
import java.util.*;

// creating the node 

class Node {
    public int frequency;
    public Node left, right;
    public char alphaFile;

    public Node(char alphaFile, int frequency) {
        this.alphaFile = alphaFile;
        this.frequency = frequency;
    }

    public Node() {}

    public String toString() {
        return alphaFile + " " + frequency;
    }

}

class FrequencyComparator implements Comparator<Node> {
    public int compare(Node one, Node two) {
        int frequencyOne = one.frequency;
        int frequencyTwo = two.frequency;
        
        return frequencyOne-frequencyTwo;
    }
}

public class Decompress_a_File {
    public static PriorityQueue < Node > priorityQueue;
    public static HashMap < String, Character > stringToCharacter;
    public static HashMap < Character, String > characterToString;
    
    // from the frequency of each character to the tree

    public static Node huffmanCode(int n) {
        Node one;
        Node two;
        for (int index = 1; index <= n - 1; index++) {
            Node three = new Node();
            three.left = one = priorityQueue.poll();
            three.right = two = priorityQueue.poll();
            three.frequency = one.frequency + two.frequency;
            priorityQueue.add(three);
        }
        return priorityQueue.poll();
    }

    public static void postorder(Node node, String character) {
        if (node == null)
            return;
        postorder(node.left, character + "0");
        postorder(node.right, character + "1");

        if (!Character.toString(node.alphaFile).equals("&#092;&#048;")) {

            // only gets nodes with keys

            System.out.println("{" + node.alphaFile + ":" + character + "}");
            characterToString.put(node.alphaFile, character);
            stringToCharacter.put(character, node.alphaFile);
        }
    }

    // table for decompression

    public static void table(Node root) {
        characterToString = new HashMap < Character, String > ();
        stringToCharacter = new HashMap < String, Character > ();
        postorder(root, new String());
    }

    // this is just here as a placeholder, it would be better to call PartOne here since it compresses 

    public static String compress(String character) {
        String compressor = new String();
        for (int index = 0; index < character.length(); index++)
            compressor = compressor + characterToString.get(character.charAt(index));
        return compressor;
    }

    public static String decompress(String character) {
        String temp = new String();
        String result = new String();
        for (int index = 0; index < character.length(); index++) {
            temp = temp + character.charAt(index);
            Character compressor = stringToCharacter.get(temp);
            if (compressor != null && compressor != 0) {
                result = result + compressor;
                temp = "";
            }
        }
        return result;
    }

    // method to save all the writing to the file

    public static void save(String one) throws FileNotFoundException {
        PrintWriter file = new PrintWriter("sourceFile.txt");
        file.print(characterToString.size());

        for (char character: characterToString.keySet())
            file.println("{" + character + ":" + characterToString.get(character) + "}");
        file.println(one);
        file.close();
    }

    // method to write to the file

    public static void write(String index) throws FileNotFoundException {
        PrintWriter file = new PrintWriter("note.txt");
        file.print(stringToCharacter.size());
        for (String character: stringToCharacter.keySet())
            file.println("{" + character + ":" + stringToCharacter.get(character) + "}");
        file.println(index);
        file.close();
    }

	
    public static void main(String[] args) throws FileNotFoundException {

        File fileBob = new File(args[0]);
   
        Scanner scanner = new Scanner(fileBob);
        String text = "";
        while (scanner.hasNext()){
            text += scanner.next();
        }

        // gets the frequency of each character

        HashMap < Character, Integer > bank = new HashMap < Character, Integer > ();
        for (int index = 0; index < text.length(); index++) {
            char one = text.charAt(index);
            if (bank.containsKey(one))
                bank.put(one, bank.get(one) + 1);
            else
                bank.put(one, 1);
        }

        // adds all the nodes to the priority queue

        priorityQueue = new PriorityQueue < Node > (100, new FrequencyComparator());
        int n = 0;
        for (Character character: bank.keySet()) {
            priorityQueue.add(new Node(character, bank.get(character)));
            n++;
        }
        Node root = huffmanCode(n);
        table(root);

        String compressed = compress(text);
        save(compressed);

        String decompressed = decompress(compressed);
        write(decompressed);
    }
    
}
