package com.itvrach.www.itvrach;

/**
 * Created by engineer on 3/3/2019.
 */

public class Events {
    public static class FragmentActivityMessage{
        private String count;
        public FragmentActivityMessage(String count ){
            this.count = count;
        }

        public String getCount(){
            return count;
        }
    }



}
