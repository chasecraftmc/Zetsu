package me.blazingtide.zetsu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Test {

    public static void main(String[] ignore) {
        final Scanner scanner = new Scanner(System.in);
        final String cmd = scanner.nextLine();
        final String[] split = cmd.split(" ");

        //Unneeded up ^

        final String label = split[0];
        final List<String> args = Arrays.asList(split).subList(1, split.length);

        final List<Cmd> cmds = Arrays.asList(
                Cmd.of("run1", 1, 1, "run test"), // run test arg1 arg2
                Cmd.of("run2", 1, 1, "run idk"), // run test arg1 arg2 arg3
                Cmd.of("run3", 2, 2, "run test test2") // run test arg1 arg2 arg3
        );

        print("debug cmd: " + label + " / " + args);
        print("debug cached commands: " + cmds);

        for (int i = args.size() - 1; i >= 0; i--) {
            List<Cmd> withinSize = new ArrayList<>();
            String arg = args.get(i);

            //Linear search
            for (Cmd cmd1 : cmds) {
                if (cmd1.labels.contains(label) && (i + 1) >= cmd1.minLength && (i + 1) <= cmd1.maxLength && cmd1.args.get(i).equals(arg)) {
                    withinSize.add(cmd1);
                }
            }

            if (!withinSize.isEmpty()) {
                Cmd real = withinSize.get(0); //Add a sort function that sorts between priority?

                print(real.id);
                break;
            }
        }
    }

    public static void print(Object str) {
        System.out.println(str);
    }

    public static class Cmd {

        public String id;
        public List<String> labels;
        public List<String> args;

        //Min length for args & max length for args (Including subcommand args)
        public int maxLength, minLength;

        @Override
        public String toString() {
            return "Cmd{" +
                    "id='" + id + '\'' +
                    ", labels=" + labels +
                    ", args=" + args +
                    ", maxLength=" + maxLength +
                    ", minLength=" + minLength +
                    '}';
        }

        public static Cmd of(String id, int minLength, int maxLength, String... strs) {
            List<String> lb = new ArrayList<>();
            List<String> ar = new ArrayList<>();

            for (String str : strs) {
                String[] split = str.split(" ");

                for (int i = 0; i < split.length; i++) {
                    if (i == 0) {
                        lb.add(split[i]);
                    } else {
                        ar.add(split[i]);
                    }
                }
            }

            Cmd cmd = new Cmd();
            cmd.id = id;
            cmd.minLength = minLength;
            cmd.maxLength = maxLength;
            cmd.labels = lb;
            cmd.args = ar;

            return cmd;
        }
    }

}
