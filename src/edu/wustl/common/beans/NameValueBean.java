/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@author Mandar Deshmukh
 *@version 1.0
 */ 
package edu.wustl.common.beans;




/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

public class NameValueBean implements Comparable
{
    private Object name=new Object();
    private Object value=new Object();
    
    public NameValueBean()
    {
        
    }
    
    public NameValueBean(Object name, Object value)
    {
        this.name = name;
        this.value = value;
    }
    
    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name.toString() ;
    }
    /**
     * @param name The name to set.
     */
    public void setName(Object name)
    {
        this.name = name;
    }
    /**
     * @return Returns the value.
     */
    public String getValue()
    {
        return value.toString();
    }
    /**
     * @param value The value to set.
     */
    public void setValue(Object value)
    {
        this.value = value;
    }
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return new String("name:"+ name.toString() +" value:"+value.toString() );
    }

	public int compareTo(Object obj)
	{
//		Logger.out.debug("In CompareTo" );
//		Logger.out.debug("ObjClass : " + obj.getClass().getName());
//		Logger.out.debug("NameClass : " +((NameValueBean)obj).getName().getClass().getName());
//		Logger.out.debug("ValueClass : " +((NameValueBean)obj).getValue().getClass().getName());

		if(obj instanceof NameValueBean)
		{
			NameValueBean nameValueBean = (NameValueBean)obj;
			if(nameValueBean.name instanceof String  )
			{
				return name.toString().toLowerCase().compareTo(nameValueBean.getName().toString().toLowerCase());
			}
			else
			{
				return compareObject(obj); 
			}
		}
		return 0;
	}

	
	private int compareObject(Object tmpobj)
	{
		NameValueBean obj = (NameValueBean)tmpobj;
		//Logger.out.debug(name.getClass()+" : " +  obj.name.getClass() );
		 
		if(name.getClass() == obj.name.getClass() )
		{
			 
			if(name.getClass().getName().equalsIgnoreCase("java.lang.Long" ))
			{
				Long numOne = (Long)name;
				Long numTwo = (Long)obj.name;
				return numOne.compareTo(numTwo );
			}
			else if(name.getClass().getName().equalsIgnoreCase("java.lang.Double" ))
			{
				Double numOne = (Double)name;
				Double numTwo = (Double)(obj.name);
				return numOne.compareTo(numTwo );
			}
			else if(name.getClass().getName().equalsIgnoreCase("java.lang.Float" ))
			{
				Float numOne = (Float)name;
				Float numTwo = (Float)(obj.name);
				return numOne.compareTo(numTwo );
			}
			else if(name.getClass().getName().equalsIgnoreCase("java.lang.Integer" ))
			{
				Integer numOne = (Integer)name;
				Integer numTwo = (Integer)(obj.name);
				return numOne.compareTo(numTwo );
			}
		}
//		Logger.out.debug("Number type didnot match");
		return 0;
	}

}
