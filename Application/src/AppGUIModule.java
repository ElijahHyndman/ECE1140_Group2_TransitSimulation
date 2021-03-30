public interface AppGUIModule<T> {
    /** Defines the behavior that all GUI's should exhibit for the TrainSimulation Application.
     *   This standardizes the behavior amongst the modules so they can be worked with in generic terms, and
     *   simplifies the storage process
     *
     *   uses the Java generic <T> to represent multiple forms of objects, since Each of your draw() methods will accept a different
     */

    public void draw(T myObject);
    /** repopulates the GUI window once with all of the values that we care about, pulled from myObject.
     * no while loops or anything, just splash all of the information from the object into the textfields and stuff!
     * @before GUI window already exists, likely is not up to date
     * @after all of the data from the myObject parameter has been used to update the values shown in the gui
     */

    public void latch(T myObject);
    /** stores the parameter myObject into the localObject data-member of this class, that way the user may just call AppGUIModule.update() for further updates
     * @before localObject may or may not reference an object of type <T>
     * @after localObject now references myObject, all AppGUIModule.update() henceforth will use values from localObject
     */

    public void update();
    /** exactly like draw(T myObject), but instead uses the locally stored object-reference set using latch(T myObject).
     *
     * this function must check that localObject is not NULL; which might happen if user calls AppGUIModule.update() without first storing a reference with AppGUIModule.latch()
     * if localObject is null, do nothing.
     * @before a local reference has been set by the user by using AppGUIModule.latch(T myObject)
     * @before the gui window may not show the most up to date data from localObject
     * @after the gui window has been redrawn, showing the most up to date data from localObject
     *
     * hint: instead of copying your code from draw(T myObject), just call draw on local object like "draw(localObject)" to make life easier
     */
}
