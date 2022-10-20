package org.fireflyest.crafttask.exception;

/**
 * @author Fireflyest
 * @see org.fireflyest.crafttask.api.Task#execute()
 * @since 1.2
 */
public class ExecuteException extends Exception {

    /**
     * 任务执行错误
     */
    public ExecuteException() {
    }

    /**
     * 任务执行错误，带详细信息
     * @param message 错误信息
     */
    public ExecuteException(String message) {
        super(message);
    }
    
    

}
