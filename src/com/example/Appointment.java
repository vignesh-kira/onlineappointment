package com.example;

import   javax.jdo.annotations.IdGeneratorStrategy ;
import   javax.jdo.annotations.IdentityType ;
import   javax.jdo.annotations.PersistenceCapable ;
import   javax.jdo.annotations.Persistent ;
import   javax.jdo.annotations.PrimaryKey ;

@PersistenceCapable ( identityType =  IdentityType. APPLICATION )
public class Appointment {
	
		@PrimaryKey
        @Persistent ( valueStrategy =  IdGeneratorStrategy. IDENTITY ) 
        private   Long  id ;
        private   Long  doctorID ;
        private   Long  patientID;
        private   String time;
        private   String date;
        
        
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
        public   String  getTime ()   {
            return  time ;
        }
        public   void  setTime ( String  time )   {
            this . time   =  time ;
        }
        public   String  getDate ()   {
            return  date ;
        }
        public   void  setDate ( String  date )   {
            this . date   =  date ;
        }
        

}
