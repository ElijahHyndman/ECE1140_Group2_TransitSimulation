package GUIInterface;

import java.util.Vector;

public interface AppGUIModule<T> {
    /** Defines the behavior that all GUI's should exhibit for the TrainSimulation Application.
     *   This standardizes the behavior amongst the modules so they can be worked with in generic terms, and
     *   simplifies the storage process
     *
     *   uses the Java generic <T> to represent multiple forms of objects, since Each of your draw() methods will accept a different
     */

    public void latch(T myObject);
    /** stores the parameter myObject into the localObject data-member of this class, that way the user may just call GUIInterface.AppGUIModule.update() for further updates
     * @before localObject may or may not reference an object of type <T>
     * @after localObject now references myObject, all GUIInterface.AppGUIModule.update() henceforth will use values from localObject
     */

    public void update();
    /** exactly like draw(T myObject), but instead uses the locally stored object-reference set using latch(T myObject).
     *
     * this function must check that localObject is not NULL; which might happen if user calls GUIInterface.AppGUIModule.update() without first storing a reference with GUIInterface.AppGUIModule.latch()
     * if localObject is null, do nothing.
     * @before a local reference has been set by the user by using GUIInterface.AppGUIModule.latch(T myObject)
     * @before the gui window may not show the most up to date data from localObject
     * @after the gui window has been redrawn, showing the most up to date data from localObject
     */
}
