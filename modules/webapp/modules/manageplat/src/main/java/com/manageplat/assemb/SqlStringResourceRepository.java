package com.manageplat.assemb;

import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResource;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

public class SqlStringResourceRepository implements StringResourceRepository {
    /**
     * Current Repository encoding.
     */
    private String encoding = StringResourceLoader.REPOSITORY_ENCODING_DEFAULT;

    /**
     * @see org.apache.velocity.runtime.resource.util.StringResourceRepository#getStringResource(String)
     */
    public StringResource getStringResource(final String name) {
        return new StringResource(name, getEncoding());
    }

    /**
     * @see org.apache.velocity.runtime.resource.util.StringResourceRepository#putStringResource(String, String)
     */
    public void putStringResource(final String name, final String body) {
        //
    }

    /**
     * @see org.apache.velocity.runtime.resource.util.StringResourceRepository#putStringResource(String, String, String)
     * @since 1.6
     */
    public void putStringResource(final String name, final String body, final String encoding) {
        //
    }

    /**
     * @see org.apache.velocity.runtime.resource.util.StringResourceRepository#removeStringResource(String)
     */
    public void removeStringResource(final String name) {
        //
    }

    /**
     * @see org.apache.velocity.runtime.resource.util.StringResourceRepository#getEncoding()
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @see org.apache.velocity.runtime.resource.util.StringResourceRepository#setEncoding(String)
     */
    public void setEncoding(final String encoding) {
        this.encoding = encoding;
    }
}