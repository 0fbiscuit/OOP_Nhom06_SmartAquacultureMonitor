package vn.edu.ptit.oop.nhom06.model;

import java.util.Random;


public class DissolvedOxygenSensor extends Sensor {
    
    private Random random;
    private double baseValue; 

    public DissolvedOxygenSensor(String sensorId, double minSafe, double maxSafe) {
        super(sensorId, "Oxy hòa tan (DO)", "mg/L", minSafe, maxSafe);
        this.random = new Random();
        
        this.baseValue = 5.0;
    }

    
    @Override
    public double read() {
        double noise = random.nextGaussian() * 1.2; 
        
        
        if (random.nextDouble() < 0.05) {
            noise -= 3.0; 
        }
        
        double currentValue = this.baseValue + noise;
        
       
        if (currentValue < 0) currentValue = 0.0;
        
        return Math.round(currentValue * 10.0) / 10.0;
    }
}
