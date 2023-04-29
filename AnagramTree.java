//
//  WORDS. An iterator that reads lower case words from a text file.
//
//    James Moen
//    12 Apr 23
//

import java.io.FileReader;   //  Read Unicode chars from a file.
import java.io.IOException;  //  In case there's IO trouble.

//  WORDS. Iterator. Read words, represented as STRINGs, from a text file. Each
//  word is the longest possible contiguous series of alphabetic ASCII CHARs.

class Words
{
    private int           ch;      //  Last CHAR from READER, as an INT.
    private FileReader    reader;  //  Read CHARs from here.
    private StringBuilder word;    //  Last word read from READER.

//  Constructor. Initialize an instance of WORDS, so it reads words from a file
//  whose pathname is PATH. Throw an exception if we can't open PATH.

    public Words(String path)
    {
        try
        {
            reader = new FileReader(path);
            ch = reader.read();
        }
        catch (IOException ignore)
        {
            throw new IllegalArgumentException("Cannot open '" + path + "'.");
        }
    }

//  HAS NEXT. Try to read a WORD from READER, converting it to lower case as we
//  go. Test if we were successful.

    public boolean hasNext()
    {
        word = new StringBuilder();
        while (ch > 0 && ! isAlphabetic((char) ch))
        {
            read();
        }
        while (ch > 0 && isAlphabetic((char) ch))
        {
            word.append(toLower((char) ch));
            read();
        }
        return word.length() > 0;
    }

//  IS ALPHABETIC. Test if CH is an ASCII letter.

    private boolean isAlphabetic(char ch)
    {
        return 'a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z';
    }

//  NEXT. If HAS NEXT is true, then return a WORD read from READER as a STRING.
//  Otherwise, return an undefined STRING.

    public String next()
    {
        return word.toString();
    }

//  READ. Read the next CHAR from READER. Set CH to the CHAR, represented as an
//  INT. If there are no more CHARs to be read from READER, then set CH to -1.

    private void read()
    {
        try
        {
            ch = reader.read();
        }
        catch (IOException ignore)
        {
            ch = -1;
        }
    }

//  TO LOWER. Return the lower case ASCII letter which corresponds to the ASCII
//  letter CH.

    private char toLower(char ch)
    {
        if ('a' <= ch && ch <= 'z')
        {
            return ch;
        }
        else
        {
            return (char) (ch - 'A' + 'a');
        }
    }

//  MAIN. For testing. Open a text file whose pathname is the 0th argument from
//  the command line. Read words from the file, and print them one per line.

    public static void main(String [] args)
    {
        Words words = new Words("");
        while (words.hasNext())
        {
            System.out.println("'" + words.next() + "'");
        }
    }
}

class AnagramTree {
    private class TreeNode {
        private byte[] summary;
        private WordNode words;
        private TreeNode left;
        private TreeNode right;
        private TreeNode(byte[] summary, WordNode words) {
            this.summary = summary;
            this.words = words;
        }
    }
    private class WordNode {
        private String word;
        private WordNode next;
        private WordNode(String word, WordNode next) {
            this.word = word;
            this.next = next;
        }
    }
    private TreeNode treeHead;
    public AnagramTree() {
        treeHead = new TreeNode(new byte[26], null); //treeHead.right is the first Node in the tree
    }
    public void add(String word) {
        byte[] currentSummary = stringToSummary(word);
        TreeNode subtree = treeHead;
        while (subtree != null) {
            int test = compareSummaries(currentSummary, subtree.summary);
            if (test < 0) {
                if (subtree.left == null) {
                    subtree.left = new TreeNode(currentSummary, new WordNode(word, null));
                    return;
                }
                else {
                    subtree = subtree.left;
                }
            }
            else if (test > 0) {
                if (subtree.right == null) {
                    subtree.right = new TreeNode(currentSummary, new WordNode(word, null));
                    return;
                }
                else {
                    subtree = subtree.right;
                }
            }
            else {
                if (isIn(word, subtree.words)) {
                    return;
                }
                else {
                    subtree.words = new WordNode(word, subtree.words);
                    return;
                }
            }
        }
    }
    private boolean isIn(String word, WordNode list) {
        WordNode temp = list;
        while (temp != null) {
            if (temp.word.equals(word)) {
                return true;
            }
            temp = temp.next;
        }
        return false;
    }
    public void anagrams() {
        //MUST USE A HELPER METHOD
        preorder(treeHead.right);
    }
    private int compareSummaries (byte[] left, byte[] right) {
        for (int index = 0; index < left.length; index++) {
            if (left[index] != right[index]) {
                return left[index] - right[index];
            }
        }
        return 0;
    }
    private byte[] stringToSummary(String word) {
        byte[] out = new byte[26];
        for (int index = 0; index < word.length(); index++) {
            out[word.charAt(index) - 'a']++;
        }
        return out;
    }
    private void preorder(TreeNode subtree) {
        if (subtree != null) {
            WordNode temp = subtree.words;
            if (temp.next != null) {
                while(temp != null) { //Traversing the words linked list
                    System.out.print(temp.word + " ");
                    temp = temp.next;
                }
                System.out.println();
            }
            preorder(subtree.left);
            preorder(subtree.right);
        }
    }
}

class Anagrammer {
    public static void main(String[] args) {
        Words book = new Words(""); //Insert file path
        AnagramTree tree = new AnagramTree();
        while (book.hasNext()) {
            String currentWord = book.next();
            tree.add(currentWord);
        }
        tree.anagrams();
    }
}