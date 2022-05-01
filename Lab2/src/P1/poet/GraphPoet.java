/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package Lab2.src.P1.poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import Lab2.src.P1.graph.ConcreteEdgesGraph;
import Lab2.src.P1.graph.ConcreteVerticesGraph;
import Lab2.src.P1.graph.Graph;
import Lab2.src.P1.graph.Vertex;
import Lab2.test.P1.graph.ConcreteEdgesGraphTest;

/**
 * A graph-based poetry generator.
 *
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 *
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 *
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 *
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 *
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 *
 * 基于图的诗歌生成器GraphPoet:
 * 功能: 使用文本语料库进行初始化，用于导出词亲和图,并给输入的字符串添加桥接词
 * 图中的顶点是单词。单词被定义为非空且不区分大小写的不含空格和换行符的字符串
 * 单词在语料库中由空格、换行符或文件结尾分隔。
 ** 图中的边计数邻接：语料中“w1”跟“w2”的次数就是 w1 到 w2 的边的权重。
 *
 * 例如，给定这个语料库：Hello, HELLO, hello, goodbye!
 * 该图将包含两条边：一条是 ("hello,") -> ("hello,"),权重为 2
 * 另一条是 ("hello,") -> ("goodbye!") 权重为 1
 * 其中顶点表示不区分大小写的 {@code "hello,"} 和 {@code "goodbye!"}.
 * 这里注意,以空格等符号分隔单词,但是不是以标点符号分割,所以单词包括标点符号
 *
 * 然后再给定一个另一个输入字符串，GraphPoet通过尝试在输入的每对相邻词之间插入一个桥词来生成一首诗。
 ** 输入词“w1”和“w2”之间的桥接词将是这样一些“b”,使得w1 -> b -> w2是从 w1 到 w2 的权重最大的路径。
 * 如果没有这样的路径，则不插入桥字。
 * 在输出诗中，输入词保持原来的大小写，而桥词是小写的。诗中每个单词之间都有一个空格。
 *
 * 例如，给定这个语料库：This is a test of the Mugar Omni Theater sound system.
 * 语料库可能是一个句子,也可能是由换行符分隔的多个句子
 * 如果输入为：Test the system.
 * 输出的诗将是：Test of the system.
 *
 ** 要求: 这是一个必需的ADT类，您不得削弱所需的规范。但是，您可以加强规范并添加其他方法。
 * 你必须在你的代表中使用 Graph，其他的实现取决于你自己。
 */
public final class GraphPoet{
    private final Graph<String> graph = Graph.empty(); // ConcreteVerticesGraph
    private final Set<String> words = new HashSet<>(); // store all the case sensitive words
    // Abstraction function:
    //     AF(graph) = a GraphPoet which is initialized with a corpus of text
    // Representation invariant:
    //     the name of every vertex in the graph does not contain space or newline characters
    //     every vertex in graph, whose mapKeySet must contain all the vertices only except itself
    // Safety from rep exposure:
    //     all the fields are final and private there are no mutable or return methods.

    /**
     * 由给出的文件corpus生成一个词亲和图
     * <p>
     * Create a new poet with the graph from corpus (as described above).
     *
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException{
        FileReader reader = new FileReader(corpus);
        BufferedReader bf = new BufferedReader(reader);
        String longString;
        String[] strings;

        while((longString = bf.readLine()) != null){ // add all the words to the vertices of graph
            strings = longString.split(" "); // read a line from the file at every turn
            for(int i = 0; i < strings.length; i++){
                words.add(strings[i]);
                strings[i] = strings[i].toLowerCase(Locale.ROOT);
                // store the word case insensitive temporarily in the GraphPoem
                // when input the poems, transform the words into case sensitive
                graph.add(strings[i]); // add the weight of the adjacent words
                if(i > 0){
                    if(graph.targets(strings[i - 1]).containsKey(strings[i]))
                        // if the edge already exist, add the weight
                        graph.set(strings[i - 1], strings[i], graph.targets(strings[i - 1]).get(strings[i]) + 1);
                    else
                        // else set the weight 1
                        graph.set(strings[i - 1], strings[i], 1);
                }
            }
        }

        for(String v0: graph.vertices())
            // initialize all weigh between every two words (concluding itself) which not exist to 0
            for(String v1: graph.vertices())
                if(!graph.targets(v0).containsKey(v1))
                    graph.set(v0, v1, 0);
        checkRep();
    }

    public void checkRep(){
        ConcreteEdgesGraph<String> graph1 = new ConcreteEdgesGraph<>();
        if(graph instanceof ConcreteEdgesGraph)
            graph1 = (ConcreteEdgesGraph<String>) graph;
        // 通过强制类型转化,再直接使用ConcreteVerticesGraph类的checkRep
        graph1.checkRep();
    }

    /**
     * Generate a poem.
     *
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input){
        StringBuilder poem = new StringBuilder();
        String[] strings = input.split(" ");
        Set<String> sensitiveSet = new HashSet<>();
        // 输入的分大小写的单词set集合
        List<String> insensitivePoem = new ArrayList<>();
        // 不分大小写的单词list数组,仅包括input中的单词
        for(String s: strings){
            insensitivePoem.add(s.toLowerCase(Locale.ROOT));
            sensitiveSet.add(s);
        }
        // 将桥接词插入到input数组中,且不能在第一个之前或最后一个之后插入
        for(int i = 0; i < insensitivePoem.size() - 1; i++){
            for(String s: graph.vertices()){
                // 每两个单词之间只允许插入一个桥接词,添加完直接break
                // 插入的词不可能是第一个单词,全部小写
                if(graph.targets(insensitivePoem.get(i)).containsKey(s) && graph.sources(insensitivePoem.get(i + 1)).containsKey(s)){
                    insensitivePoem.add(i + 1, s);
                    break;
                }
            }
        }
        // 把单词数组连接成诗句
        boolean inPrimary; // 是否是原输入中的单词
        for(int i = 0; i < insensitivePoem.size(); i++){
            inPrimary = false;
            for(String s: sensitiveSet){
                if(s.equalsIgnoreCase(insensitivePoem.get(i))){
                    // 如果输入词库中存在与该单词不分大小写相同的词,则直接写入输入词库中的词
                    poem.append(s);
                    if(i != insensitivePoem.size() - 1)
                        poem.append(" ");
                    inPrimary = true;
                    break;
                }
            }
            if(inPrimary)
                continue;
            // 否则是桥词,桥词一定是小写的且后面一定有空格
            for(String s1: words){
                if(s1.equalsIgnoreCase(insensitivePoem.get(i))){
                    poem.append(s1.toLowerCase(Locale.ROOT)).append(" ");
                    break;
                }
            }
        }
        return poem.toString();
    }

    @Override
    public String toString(){
        // 去掉graph.toString的第一行(全小写打印),加上word.toString(区分大小写打印)
        int i, j;
        String s = graph.toString();
        StringBuilder s1 = new StringBuilder();
        s1.append(words).append("\n");
        for(i = 0; i < s.length(); i++)
            if(s.charAt(i) == '\n')
                break;

        for(j = i + 1; j < s.length(); j++)
            s1.append(s.charAt(j));

        return s1.toString();
    }
}