/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package css123p_finalproject.model.npc;
import ai.onnxruntime.*;
import java.util.Collections;
import java.util.Map;

/**
 *
 * @author MSTR Xen
 */
public class Onnx {
    public static void main(String[] args) throws OrtException {
        // 1. Initialize environment and model session
        try (OrtEnvironment env = OrtEnvironment.getEnvironment();
             OrtSession.SessionOptions options = new OrtSession.SessionOptions();
             OrtSession session = env.createSession("path/to/model.onnx", options)) {
            
            // 2. Prepare inputs (e.g., using a flat array and shape specifications)
            float[] inputData = new float[]{1.0f, 2.0f, 3.0f, 4.0f};
            long[] shape = new long[]{1, 4};
            
            try (OnnxTensor inputTensor = OnnxTensor.createTensor(env, java.nio.FloatBuffer.wrap(inputData), shape)) {
                Map<String, OnnxTensor> inputs = Collections.singletonMap("input_name", inputTensor);
                
                // 3. Execute inference
                try (OrtSession.Result results = session.run(inputs)) {
                    // 4. Extract output values safely
                    OnnxValue outputValue = results.get(0);
                    float[][] outputData = (float[][]) outputValue.getValue();
                    System.out.println("Output result: " + outputData[0][0]);
                }
            }
        }
    }
}
