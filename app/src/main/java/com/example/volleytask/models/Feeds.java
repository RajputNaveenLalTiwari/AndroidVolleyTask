package com.example.volleytask.models;

/**
 * Created by 2114 on 27-06-2017.
 */

public class Feeds
{
    public int id;
    public String name;
    public String username;
    public String email;

    public Address address;
    public Address.Geo geo;
    public class Address
    {
        public String street;
        public String suite;
        public String city;
        public String zipcode;

        public class Geo
        {
            public String lat;
            public String lng;
        }
    }

    public String phone;
    public String website;

    public Company company;
    public class Company
    {
        public String name;
        public String catchPhrase;
        public String bs;
    }
}
