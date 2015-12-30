//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.26 at 02:09:24 PM VET 
//


package com.bitdubai.entities;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.bitdubai.entities package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Screenshot_QNAME = new QName("", "screenshot");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.bitdubai.entities
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Libraries }
     * 
     */
    public Libraries createLibraries() {
        return new Libraries();
    }

    /**
     * Create an instance of {@link Library }
     * 
     */
    public Library createLibrary() {
        return new Library();
    }

    /**
     * Create an instance of {@link Authors }
     * 
     */
    public Authors createAuthors() {
        return new Authors();
    }

    /**
     * Create an instance of {@link Author }
     * 
     */
    public Author createAuthor() {
        return new Author();
    }

    /**
     * Create an instance of {@link Certifications }
     * 
     */
    public Certifications createCertifications() {
        return new Certifications();
    }

    /**
     * Create an instance of {@link Certification }
     * 
     */
    public Certification createCertification() {
        return new Certification();
    }

    /**
     * Create an instance of {@link LifeCycle }
     * 
     */
    public LifeCycle createLifeCycle() {
        return new LifeCycle();
    }

    /**
     * Create an instance of {@link Status }
     * 
     */
    public Status createStatus() {
        return new Status();
    }

    /**
     * Create an instance of {@link Mantainers }
     * 
     */
    public Mantainers createMantainers() {
        return new Mantainers();
    }

    /**
     * Create an instance of {@link Mantainer }
     * 
     */
    public Mantainer createMantainer() {
        return new Mantainer();
    }

    /**
     * Create an instance of {@link Android }
     * 
     */
    public Android createAndroid() {
        return new Android();
    }

    /**
     * Create an instance of {@link Screenshots }
     * 
     */
    public Screenshots createScreenshots() {
        return new Screenshots();
    }

    /**
     * Create an instance of {@link Plugins }
     * 
     */
    public Plugins createPlugins() {
        return new Plugins();
    }

    /**
     * Create an instance of {@link Plugin }
     * 
     */
    public Plugin createPlugin() {
        return new Plugin();
    }

    /**
     * Create an instance of {@link Layers }
     * 
     */
    public Layers createLayers() {
        return new Layers();
    }

    /**
     * Create an instance of {@link Layer }
     * 
     */
    public Layer createLayer() {
        return new Layer();
    }

    /**
     * Create an instance of {@link Addons }
     * 
     */
    public Addons createAddons() {
        return new Addons();
    }

    /**
     * Create an instance of {@link Addon }
     * 
     */
    public Addon createAddon() {
        return new Addon();
    }

    /**
     * Create an instance of {@link Androids }
     * 
     */
    public Androids createAndroids() {
        return new Androids();
    }

    /**
     * Create an instance of {@link Next }
     * 
     */
    public Next createNext() {
        return new Next();
    }

    /**
     * Create an instance of {@link Step }
     * 
     */
    public Step createStep() {
        return new Step();
    }

    /**
     * Create an instance of {@link Platforms }
     * 
     */
    public Platforms createPlatforms() {
        return new Platforms();
    }

    /**
     * Create an instance of {@link Platform }
     * 
     */
    public Platform createPlatform() {
        return new Platform();
    }

    /**
     * Create an instance of {@link Dependencies }
     * 
     */
    public Dependencies createDependencies() {
        return new Dependencies();
    }

    /**
     * Create an instance of {@link Dependency }
     * 
     */
    public Dependency createDependency() {
        return new Dependency();
    }

    /**
     * Create an instance of {@link Processes }
     * 
     */
    public Processes createProcesses() {
        return new Processes();
    }

    /**
     * Create an instance of {@link Process }
     * 
     */
    public Process createProcess() {
        return new Process();
    }

    /**
     * Create an instance of {@link Steps }
     * 
     */
    public Steps createSteps() {
        return new Steps();
    }

    /**
     * Create an instance of {@link SuperLayer }
     * 
     */
    public SuperLayer createSuperLayer() {
        return new SuperLayer();
    }

    /**
     * Create an instance of {@link SuperLayers }
     * 
     */
    public SuperLayers createSuperLayers() {
        return new SuperLayers();
    }

    /**
     * Create an instance of {@link Fermat }
     * 
     */
    public Fermat createFermat() {
        return new Fermat();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "screenshot")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createScreenshot(String value) {
        return new JAXBElement<String>(_Screenshot_QNAME, String.class, null, value);
    }

}
