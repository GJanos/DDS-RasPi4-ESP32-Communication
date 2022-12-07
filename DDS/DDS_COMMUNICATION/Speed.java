

/*
WARNING: THIS FILE IS AUTO-GENERATED. DO NOT MODIFY.

This file was generated from .idl 
using RTI Code Generator (rtiddsgen) version 3.1.1.
The rtiddsgen tool is part of the RTI Connext DDS distribution.
For more information, type 'rtiddsgen -help' at a command shell
or consult the Code Generator User's Manual.
*/

import com.rti.dds.infrastructure.*;
import com.rti.dds.infrastructure.Copyable;
import java.io.Serializable;
import com.rti.dds.cdr.CdrHelper;

public class Speed   implements Copyable, Serializable{

    public String id= (String)""; /* maximum length = (256) */
    public String message= (String)""; /* maximum length = (256) */
    public long timestamp = (long)0;
    public double speed = (double)0;

    public Speed() {

    }
    public Speed (Speed other) {

        this();
        copy_from(other);
    }

    public static java.lang.Object create() {

        Speed self;
        self = new  Speed();
        self.clear();
        return self;

    }

    public void clear() {

        id = (String)"";
        message = (String)"";
        timestamp = (long)0;
        speed = (double)0;
    }

    @Override
    public boolean equals(java.lang.Object o) {

        if (o == null) {
            return false;
        }        

        if(getClass() != o.getClass()) {
            return false;
        }

        Speed otherObj = (Speed)o;

        if(!this.id.equals(otherObj.id)) {
            return false;
        }
        if(!this.message.equals(otherObj.message)) {
            return false;
        }
        if(this.timestamp != otherObj.timestamp) {
            return false;
        }
        if(this.speed != otherObj.speed) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int __result = 0;
        __result += id.hashCode(); 
        __result += message.hashCode(); 
        __result += (int)timestamp;
        __result += (int)speed;
        return __result;
    }

    /**
    * This is the implementation of the <code>Copyable</code> interface.
    * This method will perform a deep copy of <code>src</code>
    * This method could be placed into <code>SpeedTypeSupport</code>
    * rather than here by using the <code>-noCopyable</code> option
    * to rtiddsgen.
    * 
    * @param src The Object which contains the data to be copied.
    * @return Returns <code>this</code>.
    * @exception NullPointerException If <code>src</code> is null.
    * @exception ClassCastException If <code>src</code> is not the 
    * same type as <code>this</code>.
    * @see com.rti.dds.infrastructure.Copyable#copy_from(java.lang.Object)
    */
    public java.lang.Object copy_from(java.lang.Object src) {

        Speed typedSrc = (Speed) src;
        Speed typedDst = this;

        typedDst.id = typedSrc.id;
        typedDst.message = typedSrc.message;
        typedDst.timestamp = typedSrc.timestamp;
        typedDst.speed = typedSrc.speed;

        return this;
    }

    @Override
    public java.lang.String toString(){
        return toString("", 0);
    }

    public java.lang.String toString(java.lang.String desc, int indent) {
        java.lang.StringBuffer strBuffer = new java.lang.StringBuffer();

        if (desc != null) {
            CdrHelper.printIndent(strBuffer, indent);
            strBuffer.append(desc).append(":\n");
        }

        CdrHelper.printIndent(strBuffer, indent+1);        
        strBuffer.append("id: ").append(this.id).append("\n");  
        CdrHelper.printIndent(strBuffer, indent+1);        
        strBuffer.append("message: ").append(this.message).append("\n");  
        CdrHelper.printIndent(strBuffer, indent+1);        
        strBuffer.append("timestamp: ").append(this.timestamp).append("\n");  
        CdrHelper.printIndent(strBuffer, indent+1);        
        strBuffer.append("speed: ").append(this.speed).append("\n");  

        return strBuffer.toString();
    }

}
