package com.oblak.sd;


import com.oblak.sd.model.Response;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api")
@RestController()
public class Api {

    public static final DecimalFormat df = new DecimalFormat("#.##");

    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("It works");
    }

    @PostMapping("/calculate")
    public ResponseEntity<Response> calculate(@RequestBody MultipartFile file){

     try {
         StopWatch stopWatch = new StopWatch();
         stopWatch.start();

         List<Integer> numbers = readFromFile(file);
         Double result = calculateStandardDeviation(numbers);

         stopWatch.stop();
         Response response = new Response();
         response.setTimeTaken(stopWatch.getTotalTimeMillis()+" ms");
         response.setResult(Double.valueOf(df.format(result)));

         return ResponseEntity.ok(response);
     } catch (IOException e) {
         return ResponseEntity.status(HttpStatusCode.valueOf(500)).build();
     }
    }

    private List<Integer> readFromFile(MultipartFile file) throws IOException{
        List<Integer> numbers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            String line;
            while ((line = reader.readLine()) != null) {
                numbers.add(Integer.parseInt(line));
            }
        }
        return numbers;
    }
    private Double calculateStandardDeviation(List<Integer> numbers){
        int n = numbers.size();
        double sum = 0.0;
        for (int num : numbers){
            sum +=num;
        }
        double mean = sum/n;
        double sumOfsqDiff = 0.0;
        for (int num: numbers){
            double diffrence = num - mean;
            sumOfsqDiff+=diffrence * diffrence;
        }
        return Math.sqrt(sumOfsqDiff/n);
    }
}
