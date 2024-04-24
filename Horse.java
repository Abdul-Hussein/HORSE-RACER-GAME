
/**
 * 
 	The horse class is used to set attributes of horses and makes modifiers available,
 		
 
 * 
 * @author (Abdirahman Hussein) 
 * @version (1)
 *//
public class Horse
{
    //Fields of class Horse
    
    private String horseName;
    private char horseSymbol;
    private boolean hasFallen;
    private double horseConfidence;
	private int distance;
	      
    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
    this.horseSymbol=horseSymbol;
    this.horseName=horseName;
    this.horseConfdidence=horseConfidence;   
    }
    
    
    
    //Other methods of class Horse
    public void fall()
    {
        this.hasFallen=true;
    }
    
    public double getConfidence()
    {
        return horseConfidence;
    }
    
    
    public int getDistanceTravelled()
    {
    return distance;    
    }
    
    public String getName()
    {
        return name;
    }
    
    public char getSymbol()
    {
        return horseSymbol;
    }
    
    public void goBackToStart()
    {
	distance=0;       
    }
    
    public boolean hasFallen()
    {
    	   return hasFallen;
    }

    public void moveForward()
    {
        distance++;
    }

    public void setConfidence(double newConfidence) throws IllegalArgumentException
    {
    if(newConfidence>=0&&newConfidence<=1)
    try{
            this.horseConfidence= newConfidence;
        }
        catch(Exception e ){
        e.printStackTrace(e);
        } 
        }
    
    public void setSymbol(char newSymbol)
    {
        this.horseSymbol = newSymbol;
    }
    
}


	MY REPORT 
	PART 1
	
	The accessor methods also known as the getters, are used to get the attributes associated 
	with the class. In this case, the name, character, if the horseHasConfidence etc.
	The accessor methods return the attributes of the class. To get a specific attribute, 
	we set it using the class constructor. The constructor takes in all attributes. The 
	accessor methods return a specific attribute so to use the constructor, we have to set
	all horse attributes e.g. we can new Horse("Adrian", '🐎',0.5) using the constructor 
	and calling the getName() in this case will return Adrian.
	
	The modifier methods can be used to modify the attribute for that object. We used the 
	constructor to set attributes for a horse named Adria, now let us assume that Adrian has
	won a decent amount of races and therefore has more confidence, then we should be able to
	 modify the conf
	 idence rating for Adrian. We can do so by calling a modifier, that just sets a new attribute 
	 e.g. setConfidence(0.7)
	
	
	
	

