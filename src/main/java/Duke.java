import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Duke {
    private static ArrayList<Task> storage = new ArrayList<Task>();
    private static File f = null;
    private static String pad(String val) {
        return "---------------------------\n" + val + "\n---------------------------";
    }

    private static String storageToStr() {
        String result = "";
        for (int i = 0; i < storage.size(); i++) {
            result += storage.get(i).storageStr();
            result += "\n";
        }
        System.out.println(result);
        return result;
    }

    private static void loadDukeList(String filePath) throws DukeException {
        File f = new File(filePath);
        if (!f.exists()) {
            f.getParentFile().mkdirs();
            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new DukeException(e.getMessage());
            }
        }
        try {
            Scanner s = new Scanner(f); // create a Scanner using the File as the source
            while (s.hasNext()) {
                // convert text to Task
                String[] input = s.nextLine().split(" \\| ");
                String type = input[0];
                String mark = input[1];
                Task x = null;
                if (type.equals("T")) {
                    x = new Todo(input[2]);
                } else if (type.equals("D")) {
                    x = new Deadline(input[2], input[3]);
                } else if (type.equals("E")) {
                    x = new Event(input[2], input[3], input[4]);
                }
                if (mark.equals("1")) {
                    x.mark();
                }
                storage.add(x);
            }
        } catch (Exception e) {
            throw new DukeException(e.getMessage());
        }

    }

    public static void main(String[] args) throws DukeException {
        try {
            loadDukeList("data/duke.txt");
        } catch (Exception e) {
            throw new DukeException(e.getMessage());
        }
        Scanner sc = new Scanner(System.in);
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println(logo + "Hello! I'm Duke. How may I be of assistance?");
        String str = sc.nextLine();
        while (true){
            String[] cmdDetails = str.split(" ", 2);
            String cmd = cmdDetails[0];
            String details = "";
            if (str.equals("bye")) {
                // exit
                break;
            }
            if (str.equals("list")) {
                // list tasks

                System.out.println("---------------------------");
                if (storage.isEmpty()) {
                    System.out.println("No tasks added yet.");
                } else {
                    for (int i = 0; i < storage.size(); i++) {
                        System.out.println(Integer.toString(i + 1) + ". " + storage.get(i).toString());
                    }
                }
                System.out.println("---------------------------");
                str = sc.nextLine();
                continue;
            }
            int len = cmdDetails.length;
            if (len > 1) {
                details = cmdDetails[1];
            }
            try {
                if (cmd.equals("mark")) {
                    if (storage.isEmpty() || len == 1) {
                        throw new DukeException("Unable to mark.");
                    }
                    // mark task as done
                    Task curr = storage.get(Integer.valueOf(details) - 1);
                    System.out.println(pad(curr.outputMarked() + curr.toString()));
                } else if (cmd.equals("unmark")) {
                    if (storage.isEmpty() || len == 1) {
                        throw new DukeException("Unable to unmark.");
                    }
                    // mark task as undone
                    Task curr = storage.get(Integer.valueOf(details) - 1);
                    System.out.println(pad(curr.outputUnmarked() + curr.toString()));
                } else if (cmd.equals("todo")) {
                    if (len == 1) {
                        throw new DukeException("Description of todo cannot be empty.");
                    }
                    Todo addition = new Todo(details);
                    storage.add(addition);
                    System.out.println(pad("Understood. I have added the task:\n" + addition.toString()
                            + "\nThere are now " + Integer.toString(storage.size()) + " task(s) in the list."));
                } else if (cmd.equals("deadline")) {
                    String[] temp = details.split(" /by ");
                    if (len == 1 || temp.length != 2) {
                        throw new DukeException("Description of deadline cannot be incomplete.");
                    }
                    Deadline addition = new Deadline(temp[0], temp[1]);
                    storage.add(addition);
                    System.out.println(pad("Understood. I have added the task:\n" + addition.toString()
                            + "\nThere are now " + Integer.toString(storage.size()) + " task(s) in the list."));
                } else if (cmd.equals("event")) {
                    String[] temp = details.split(" /from ");
                    if (len == 1 || temp.length != 2) {
                        throw new DukeException("Description of event cannot be incomplete.");
                    }
                    String[] temp2 = temp[1].split(" /to ");
                    if (temp2.length != 2) {
                        throw new DukeException("Description of event cannot be incomplete.");
                    }
                    Event addition = new Event(temp[0], temp2[0], temp2[1]);
                    storage.add(addition);
                    System.out.println(pad("Understood. I have added the task:\n" + addition.toString()
                            + "\nThere are now " + Integer.toString(storage.size()) + " task(s) in the list."));
                } else if (cmd.equals("delete")) {
                    if (storage.isEmpty() || len == 1) {
                        throw new DukeException("Unable to delete.");
                    }
                    Task curr = storage.get(Integer.valueOf(details) - 1);
                    storage.remove(Integer.valueOf(details) - 1);
                    System.out.println(pad("Noted. I have removed the task:\n" + curr.toString()
                            + "\nThere are now " + Integer.toString(storage.size()) + " task(s) in the list."));
                }
                else {
                    throw new DukeException("I do not quite understand that.");
                }
            } catch (DukeException err) {
                System.out.println(pad(err.getMessage()));
            }
            str = sc.nextLine();
        }
        try {
            FileWriter fw = new FileWriter("data/duke.txt");
            fw.write(storageToStr());
            fw.close();
        } catch (Exception e) {
            throw new DukeException(e.getMessage());
        }

        System.out.println(pad("Thank you for your patronage. Goodbye!"));
    }
}

