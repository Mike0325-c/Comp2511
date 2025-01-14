package cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import tributary.Tributary;

public class TributaryCLI {
    private static void createCommand(String[] input, Tributary tributary) {
        switch (input[1]) {
            case "topic":
                tributary.createTopic(input[2], input[3]);
                System.out.println(tributary.getTopic(input[2])); break;
            case "partition":
                tributary.createPartition(input[2], input[3]);
                System.out.println(tributary.getPartition(input[3])); break;
            case "consumer":
                if (input[2].equals("group")) {
                    tributary.createConsumerGroup(input[3], input[4]);
                    System.out.println(tributary.getConsumerGroup(input[3])); break;
                }
                tributary.createConsumer(input[2], input[3]);
                System.out.println(tributary.getConsumer(input[3])); break;
            case "producer":
                tributary.createProducer(input[2], input[3], input[4]);
                System.out.println(tributary.getProducer(input[2])); break;
            default:
        }
    }

    private static void showCommand(String[] input, Tributary tributary) {
        switch (input[1]) {
            case "topic":
                System.out.println(tributary.getTopic(input[2])); break;
            case "consumer":
                if (!input[2].equals("group")) {
                    break;
                }
                System.out.println(tributary.getConsumerGroup(input[3])); break;
            default:
        }
    }

    private static JSONObject loadFile(String input) {
        String eventFile = String.format("/cli/event/%s.json", input);
        JSONObject event;
        try {
            event = new JSONObject(FileLoader.loadResourceFile(eventFile));
        } catch (IOException e) {
            event = null;
        }
        return event;
    }

    private static void produceCommand(String[] input, Tributary tributary) {
        if (!input[1].equals("event")) {
            return;
        }
        JSONObject file = loadFile(input[4]);
        if (file.getString("Payload").equals("Integer")) {
            Integer value = file.getInt("Value");
            if (input.length == 5) {
                tributary.produceEvent(input[2], input[3], value);
                System.out.println(tributary.getTopic(input[3]));
                return;
            }
            tributary.produceEvent(input[2], input[3], value, input[4]);
            System.out.println(tributary.getTopic(input[3]));
        } else if (file.getString("Payload").equals("String")) {
            String value2 = file.getString("Value");
            if (input.length == 5) {
                tributary.produceEvent(input[2], input[3], value2);
                System.out.println(tributary.getTopic(input[3]));
                return;
            }
            tributary.produceEvent(input[2], input[3], value2, input[4]);
            System.out.println(tributary.getTopic(input[3]));
        }
    }

    private static void consumeCommand(String[] input, Tributary tributary) {
        if (!(input[1].equals("event") || input[1].equals("events"))
            || (input[1].equals("events") && input[4] == null)) {
            return;
        }
        int num = 1;
        if (input.length == 6) {
            num = Integer.parseInt(input[4]);
        }
        for (int i = 0; i < num; i++) {
            tributary.consumeEvent(input[2], input[3]);
            System.out.println(tributary.getConsumer(input[2]));
        }
    }

    private static void parallelCommand(String[] input, Tributary tributary) {
        switch (input[1]) {
            case "produce":
                List<List<String>> arrayList = new ArrayList<>();
                int num = (input.length - 2) / 3;
                for (int i = 0; i < num; i++) {
                    List<String> list = new ArrayList<>();
                    list.add(input[2 + i]);
                    list.add(input[3 + i]);
                    list.add(input[4 + i]);
                    arrayList.add(list);
                }
                parallelProduce(arrayList, tributary); break;
            case "consume":
                List<List<String>> arrayList2 = new ArrayList<>();
                int num2 = (input.length - 2) / 2;
                for (int i = 0; i < num2; i++) {
                    List<String> list = new ArrayList<>();
                    list.add(input[2 + i]);
                    list.add(input[3 + i]);
                    arrayList2.add(list);
                }
                parallelConsume(arrayList2, tributary); break;
            default:
        }
    }

    private static void parallelConsume(List<List<String>> list, Tributary tributary) {
        list.parallelStream().forEach(e -> {
            tributary.consumeEvent(e.get(0), e.get(1));
            System.out.println(tributary.getConsumer(e.get(0)));
        });
    }

    private static void parallelProduce(List<List<String>> list, Tributary tributary) {
        list.parallelStream().forEach(e -> {
            tributary.produceEvent(e.get(0), e.get(1), e.get(2));
            System.out.println(tributary.getTopic(e.get(1)));
        });
    }

    public static void main(String[] args) throws IOException {
        Tributary controller = new Tributary();
        System.out.println("Please enter your command:");
        while (true) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input = reader.readLine();
            String[] command = input.split(" ");

            switch (command[0]) {
                case "create":
                    createCommand(command, controller); break;
                case "produce":
                    produceCommand(command, controller); break;
                case "consume":
                    consumeCommand(command, controller); break;
                case "show":
                    showCommand(command, controller); break;
                case "parallel":
                    parallelCommand(command, controller); break;
                default:
            }
        }
    }
}
