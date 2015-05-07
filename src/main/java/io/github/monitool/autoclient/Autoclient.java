package io.github.monitool.autoclient;

import io.github.monitool.autoclient.quartz.ReadScheduler;
import javax.xml.ws.http.HTTPException;

/**
 * Created by Bartosz Głowacki on 2015-03-28.
 */
public class Autoclient {


    public static void main(String[] args) {
        try {
            Mode mode = args.length>0?Mode.fromString(args[0]):Mode.MEM;
            ReadScheduler.start("0/10 * * * * ?", mode);
        } catch (IllegalArgumentException e){
            System.out.println("Invalid parameter!\nExample of use: java -jar monitool.jar c\nc - sorting by cpu load\nm - sorting by memory load\nd - sorting by disk load");
            System.exit(1);
        } catch(HTTPException e){
            System.out.println("Http response " + e.getStatusCode());
            System.exit(1);
        }
    }

}
