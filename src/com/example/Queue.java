package com.example;

import   javax.jdo.annotations.IdGeneratorStrategy ;
import   javax.jdo.annotations.IdentityType ;
import   javax.jdo.annotations.PersistenceCapable ;
import   javax.jdo.annotations.Persistent ;
import   javax.jdo.annotations.PrimaryKey ;

@PersistenceCapable ( identityType =  IdentityType. APPLICATION )
public class Queue {
	
		@PrimaryKey
        @Persistent ( valueStrategy =  IdGeneratorStrategy. IDENTITY ) 
        private   Long  id ;
        private   Long  doctorID ;
        private   Long  patientID;
                
        
        public   Long  getId ()   {
                return  id ;
        }
        public   void  setId ( Long  id )   {
                this . id   =  id ;
        }
        public   Long  getDoctorID ()   {
                return  doctorID ;
        }
        public   void  setDoctorID ( Long  doctorID )   {
                this . doctorID   =  doctorID ;
        }
        public   Long  getPatientID ()   {
            return  patientID ;
        }
        public   void  setPatientID ( Long  patientID )   {
             this . patientID   =  patientID ;
        }
        
        

}
