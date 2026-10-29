package vn.edu.ptit.oop.nhom06.model;

import java.util.Random;

public class PHSensor extends Sensor {
    
    private Random random;
    private double baseValue; 

    public PHSensor(String sensorId, double minSafe, double maxSafe) {
        super(sensorId, "Độ pH (pH Level)", "pH", minSafe, maxSafe);
        this.random = new Random();
        
        this.baseValue = 8.0;
    }

    
    @Override
    public double read() {
        
        double noise = random.nextGaussian() * 0.4;
        double currentValue = this.baseValue + noise;
        
        
        return Math.round(currentValue * 100.0) / 100.0;
    }
}
