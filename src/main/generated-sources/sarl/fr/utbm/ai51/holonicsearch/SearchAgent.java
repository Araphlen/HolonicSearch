package fr.utbm.ai51.holonicsearch;

import com.google.common.base.Objects;
import io.sarl.api.core.AgentTask;
import io.sarl.api.core.DefaultContextInteractions;
import io.sarl.api.core.Initialize;
import io.sarl.api.core.InnerContextAccess;
import io.sarl.api.core.Lifecycle;
import io.sarl.api.core.Logging;
import io.sarl.api.core.ParticipantJoined;
import io.sarl.api.core.ParticipantLeft;
import io.sarl.api.core.Schedules;
import io.sarl.lang.core.Address;
import io.sarl.lang.core.Agent;
import io.sarl.lang.core.AtomicSkillReference;
import io.sarl.lang.core.DynamicSkillProvider;
import io.sarl.lang.core.Event;
import io.sarl.lang.core.Scope;
import io.sarl.lang.core.annotation.ImportedCapacityFeature;
import io.sarl.lang.core.annotation.PerceptGuardEvaluator;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import io.sarl.lang.core.util.SerializableProxy;
import java.io.File;
import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.13")
@SarlElementType(19)
@SuppressWarnings("all")
public class SearchAgent extends Agent {
  private UUID parentID;

  private String agentPath;

  private String searchPredicate;

  private ConcurrentHashMap<UUID, String> childUIDs = new ConcurrentHashMap<UUID, String>();

  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info("Search agent was started.");
  }

  private void $behaviorUnit$SearchRequest$1(final SearchRequest occurrence) {
    boolean _equals = Objects.equal(this.parentID, null);
    if (_equals) {
      this.parentID = occurrence.getSource().getID();
      this.agentPath = occurrence.path;
      this.searchPredicate = occurrence.predicate;
    }
    Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info((("Agent " + this.agentPath) + " received search request"));
    this.SearchFileInDir();
  }

  protected AgentTask SearchFileInDir() {
    AgentTask _xblockexpression = null;
    {
      File agentDirFile = new File(this.agentPath);
      File[] listFilesInDir = agentDirFile.listFiles();
      for (final File file : listFilesInDir) {
        boolean _isDirectory = file.isDirectory();
        if (_isDirectory) {
          final UUID aid = UUID.randomUUID();
          this.childUIDs.put(aid, file.getAbsolutePath());
          Lifecycle _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER();
          InnerContextAccess _$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER();
          _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER.spawnInContextWithID(SearchAgent.class, aid, _$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER.getInnerContext());
        } else {
          boolean _contains = file.getName().contains(this.searchPredicate);
          if (_contains) {
            DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
            String _absolutePath = file.getAbsolutePath();
            FileFound _fileFound = new FileFound(_absolutePath);
            class $SerializableClosureProxy implements Scope<Address> {
              
              private final UUID $_parentID;
              
              public $SerializableClosureProxy(final UUID $_parentID) {
                this.$_parentID = $_parentID;
              }
              
              @Override
              public boolean matches(final Address it) {
                UUID _iD = it.getID();
                return Objects.equal(_iD, $_parentID);
              }
            }
            final Scope<Address> _function = new Scope<Address>() {
              @Override
              public boolean matches(final Address it) {
                UUID _iD = it.getID();
                return Objects.equal(_iD, SearchAgent.this.parentID);
              }
              private Object writeReplace() throws ObjectStreamException {
                return new SerializableProxy($SerializableClosureProxy.class, SearchAgent.this.parentID);
              }
            };
            _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_fileFound, _function);
          }
        }
      }
      AgentTask _xifexpression = null;
      int _size = this.childUIDs.size();
      if ((_size <= 0)) {
        Schedules _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER();
        final Procedure1<Agent> _function_1 = (Agent it) -> {
          Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
          _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info((("Search finished in file :" + this.agentPath) + " agent kill himself"));
          Lifecycle _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER_1 = this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER();
          _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER_1.killMe();
        };
        _xifexpression = _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER.in(1000, _function_1);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }

  private void $behaviorUnit$ParticipantJoined$2(final ParticipantJoined occurrence) {
    boolean _containsKey = this.childUIDs.containsKey(occurrence.getSource().getID());
    if (_containsKey) {
      InnerContextAccess _$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER();
      String _get = this.childUIDs.get(occurrence.getSource().getID());
      SearchRequest _searchRequest = new SearchRequest(_get, this.searchPredicate);
      class $SerializableClosureProxy implements Scope<Address> {
        
        private final UUID $_iD;
        
        public $SerializableClosureProxy(final UUID $_iD) {
          this.$_iD = $_iD;
        }
        
        @Override
        public boolean matches(final Address it) {
          UUID _iD = it.getID();
          return Objects.equal(_iD, $_iD);
        }
      }
      final Scope<Address> _function = new Scope<Address>() {
        @Override
        public boolean matches(final Address it) {
          UUID _iD = it.getID();
          UUID _iD_1 = occurrence.getSource().getID();
          return Objects.equal(_iD, _iD_1);
        }
        private Object writeReplace() throws ObjectStreamException {
          return new SerializableProxy($SerializableClosureProxy.class, occurrence.getSource().getID());
        }
      };
      _$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER.getInnerContext().getDefaultSpace().emit(this.getID(), _searchRequest, _function);
    }
  }

  @SyntheticMember
  @Pure
  private boolean $behaviorUnitGuard$ParticipantJoined$2(final ParticipantJoined it, final ParticipantJoined occurrence) {
    UUID _contextID = occurrence.spaceID.getContextID();
    InnerContextAccess _$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER();
    UUID _iD = _$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER.getInnerContext().getID();
    boolean _equals = Objects.equal(_contextID, _iD);
    return _equals;
  }

  private void $behaviorUnit$FileFound$3(final FileFound occurrence) {
    DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
    FileFound _fileFound = new FileFound(occurrence.filePath);
    class $SerializableClosureProxy implements Scope<Address> {
      
      private final UUID $_parentID;
      
      public $SerializableClosureProxy(final UUID $_parentID) {
        this.$_parentID = $_parentID;
      }
      
      @Override
      public boolean matches(final Address it) {
        UUID _iD = it.getID();
        return Objects.equal(_iD, $_parentID);
      }
    }
    final Scope<Address> _function = new Scope<Address>() {
      @Override
      public boolean matches(final Address it) {
        UUID _iD = it.getID();
        return Objects.equal(_iD, SearchAgent.this.parentID);
      }
      private Object writeReplace() throws ObjectStreamException {
        return new SerializableProxy($SerializableClosureProxy.class, SearchAgent.this.parentID);
      }
    };
    _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_fileFound, _function);
  }

  private void $behaviorUnit$ParticipantLeft$4(final ParticipantLeft occurrence) {
    UUID childID = occurrence.getSource().getID();
    boolean _containsKey = this.childUIDs.containsKey(childID);
    if (_containsKey) {
      this.childUIDs.remove(childID);
      int _size = this.childUIDs.size();
      if ((_size <= 0)) {
        Schedules _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER();
        final Procedure1<Agent> _function = (Agent it) -> {
          Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
          _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info((("No more child " + this.agentPath) + " agent kill himself"));
          Lifecycle _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER();
          _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER.killMe();
        };
        _$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER.in(1000, _function);
      }
    }
  }

  @SyntheticMember
  @Pure
  private boolean $behaviorUnitGuard$ParticipantLeft$4(final ParticipantLeft it, final ParticipantLeft occurrence) {
    UUID _contextID = occurrence.spaceID.getContextID();
    InnerContextAccess _$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER();
    UUID _iD = _$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER.getInnerContext().getID();
    boolean _equals = Objects.equal(_contextID, _iD);
    return _equals;
  }

  @Extension
  @ImportedCapacityFeature(Logging.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_LOGGING;

  @SyntheticMember
  @Pure
  private Logging $CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING == null || this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING = $getSkill(Logging.class);
    }
    return $castSkill(Logging.class, this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING);
  }

  @Extension
  @ImportedCapacityFeature(Lifecycle.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE;

  @SyntheticMember
  @Pure
  private Lifecycle $CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE == null || this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE = $getSkill(Lifecycle.class);
    }
    return $castSkill(Lifecycle.class, this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE);
  }

  @Extension
  @ImportedCapacityFeature(Schedules.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES;

  @SyntheticMember
  @Pure
  private Schedules $CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES == null || this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES = $getSkill(Schedules.class);
    }
    return $castSkill(Schedules.class, this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES);
  }

  @Extension
  @ImportedCapacityFeature(InnerContextAccess.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS;

  @SyntheticMember
  @Pure
  private InnerContextAccess $CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS == null || this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS = $getSkill(InnerContextAccess.class);
    }
    return $castSkill(InnerContextAccess.class, this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS);
  }

  @Extension
  @ImportedCapacityFeature(DefaultContextInteractions.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS;

  @SyntheticMember
  @Pure
  private DefaultContextInteractions $CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS == null || this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS = $getSkill(DefaultContextInteractions.class);
    }
    return $castSkill(DefaultContextInteractions.class, this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS);
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Initialize(final Initialize occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Initialize$0(occurrence));
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$ParticipantLeft(final ParticipantLeft occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    if ($behaviorUnitGuard$ParticipantLeft$4(occurrence, occurrence)) {
      ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$ParticipantLeft$4(occurrence));
    }
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$ParticipantJoined(final ParticipantJoined occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    if ($behaviorUnitGuard$ParticipantJoined$2(occurrence, occurrence)) {
      ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$ParticipantJoined$2(occurrence));
    }
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$FileFound(final FileFound occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$FileFound$3(occurrence));
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$SearchRequest(final SearchRequest occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$SearchRequest$1(occurrence));
  }

  @SyntheticMember
  @Override
  public void $getSupportedEvents(final Set<Class<? extends Event>> toBeFilled) {
    super.$getSupportedEvents(toBeFilled);
    toBeFilled.add(FileFound.class);
    toBeFilled.add(SearchRequest.class);
    toBeFilled.add(Initialize.class);
    toBeFilled.add(ParticipantJoined.class);
    toBeFilled.add(ParticipantLeft.class);
  }

  @SyntheticMember
  @Override
  public boolean $isSupportedEvent(final Class<? extends Event> event) {
    if (FileFound.class.isAssignableFrom(event)) {
      return true;
    }
    if (SearchRequest.class.isAssignableFrom(event)) {
      return true;
    }
    if (Initialize.class.isAssignableFrom(event)) {
      return true;
    }
    if (ParticipantJoined.class.isAssignableFrom(event)) {
      return true;
    }
    if (ParticipantLeft.class.isAssignableFrom(event)) {
      return true;
    }
    return false;
  }

  @SyntheticMember
  @Override
  public void $evaluateBehaviorGuards(final Object event, final Collection<Runnable> callbacks) {
    super.$evaluateBehaviorGuards(event, callbacks);
    if (event instanceof FileFound) {
      final FileFound occurrence = (FileFound) event;
      $guardEvaluator$FileFound(occurrence, callbacks);
    }
    if (event instanceof SearchRequest) {
      final SearchRequest occurrence = (SearchRequest) event;
      $guardEvaluator$SearchRequest(occurrence, callbacks);
    }
    if (event instanceof Initialize) {
      final Initialize occurrence = (Initialize) event;
      $guardEvaluator$Initialize(occurrence, callbacks);
    }
    if (event instanceof ParticipantJoined) {
      final ParticipantJoined occurrence = (ParticipantJoined) event;
      $guardEvaluator$ParticipantJoined(occurrence, callbacks);
    }
    if (event instanceof ParticipantLeft) {
      final ParticipantLeft occurrence = (ParticipantLeft) event;
      $guardEvaluator$ParticipantLeft(occurrence, callbacks);
    }
  }

  @Override
  @Pure
  @SyntheticMember
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SearchAgent other = (SearchAgent) obj;
    if (!java.util.Objects.equals(this.parentID, other.parentID))
      return false;
    if (!java.util.Objects.equals(this.agentPath, other.agentPath))
      return false;
    if (!java.util.Objects.equals(this.searchPredicate, other.searchPredicate))
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + java.util.Objects.hashCode(this.parentID);
    result = prime * result + java.util.Objects.hashCode(this.agentPath);
    result = prime * result + java.util.Objects.hashCode(this.searchPredicate);
    return result;
  }

  @SyntheticMember
  public SearchAgent(final UUID parentID, final UUID agentID) {
    super(parentID, agentID);
  }

  @SyntheticMember
  @Inject
  public SearchAgent(final UUID parentID, final UUID agentID, final DynamicSkillProvider skillProvider) {
    super(parentID, agentID, skillProvider);
  }
}
