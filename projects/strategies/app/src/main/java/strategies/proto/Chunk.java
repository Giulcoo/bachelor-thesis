// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Data.proto

package strategies.proto;

/**
 * Protobuf type {@code strategies.Chunk}
 */
public final class Chunk extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:strategies.Chunk)
    ChunkOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Chunk.newBuilder() to construct.
  private Chunk(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Chunk() {
    id_ = "";
    players_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new Chunk();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return strategies.proto.Data.internal_static_strategies_Chunk_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return strategies.proto.Data.internal_static_strategies_Chunk_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            strategies.proto.Chunk.class, strategies.proto.Chunk.Builder.class);
  }

  private int bitField0_;
  public static final int ID_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private volatile java.lang.Object id_ = "";
  /**
   * <code>string id = 1;</code>
   * @return The id.
   */
  @java.lang.Override
  public java.lang.String getId() {
    java.lang.Object ref = id_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      id_ = s;
      return s;
    }
  }
  /**
   * <code>string id = 1;</code>
   * @return The bytes for id.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getIdBytes() {
    java.lang.Object ref = id_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      id_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int PLAYERS_FIELD_NUMBER = 2;
  @SuppressWarnings("serial")
  private java.util.List<strategies.proto.Player> players_;
  /**
   * <code>repeated .strategies.Player players = 2;</code>
   */
  @java.lang.Override
  public java.util.List<strategies.proto.Player> getPlayersList() {
    return players_;
  }
  /**
   * <code>repeated .strategies.Player players = 2;</code>
   */
  @java.lang.Override
  public java.util.List<? extends strategies.proto.PlayerOrBuilder> 
      getPlayersOrBuilderList() {
    return players_;
  }
  /**
   * <code>repeated .strategies.Player players = 2;</code>
   */
  @java.lang.Override
  public int getPlayersCount() {
    return players_.size();
  }
  /**
   * <code>repeated .strategies.Player players = 2;</code>
   */
  @java.lang.Override
  public strategies.proto.Player getPlayers(int index) {
    return players_.get(index);
  }
  /**
   * <code>repeated .strategies.Player players = 2;</code>
   */
  @java.lang.Override
  public strategies.proto.PlayerOrBuilder getPlayersOrBuilder(
      int index) {
    return players_.get(index);
  }

  public static final int POSITION_FIELD_NUMBER = 3;
  private strategies.proto.Vector position_;
  /**
   * <code>.strategies.Vector position = 3;</code>
   * @return Whether the position field is set.
   */
  @java.lang.Override
  public boolean hasPosition() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <code>.strategies.Vector position = 3;</code>
   * @return The position.
   */
  @java.lang.Override
  public strategies.proto.Vector getPosition() {
    return position_ == null ? strategies.proto.Vector.getDefaultInstance() : position_;
  }
  /**
   * <code>.strategies.Vector position = 3;</code>
   */
  @java.lang.Override
  public strategies.proto.VectorOrBuilder getPositionOrBuilder() {
    return position_ == null ? strategies.proto.Vector.getDefaultInstance() : position_;
  }

  public static final int SIZE_FIELD_NUMBER = 4;
  private strategies.proto.Vector size_;
  /**
   * <code>.strategies.Vector size = 4;</code>
   * @return Whether the size field is set.
   */
  @java.lang.Override
  public boolean hasSize() {
    return ((bitField0_ & 0x00000002) != 0);
  }
  /**
   * <code>.strategies.Vector size = 4;</code>
   * @return The size.
   */
  @java.lang.Override
  public strategies.proto.Vector getSize() {
    return size_ == null ? strategies.proto.Vector.getDefaultInstance() : size_;
  }
  /**
   * <code>.strategies.Vector size = 4;</code>
   */
  @java.lang.Override
  public strategies.proto.VectorOrBuilder getSizeOrBuilder() {
    return size_ == null ? strategies.proto.Vector.getDefaultInstance() : size_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(id_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, id_);
    }
    for (int i = 0; i < players_.size(); i++) {
      output.writeMessage(2, players_.get(i));
    }
    if (((bitField0_ & 0x00000001) != 0)) {
      output.writeMessage(3, getPosition());
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      output.writeMessage(4, getSize());
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(id_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, id_);
    }
    for (int i = 0; i < players_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, players_.get(i));
    }
    if (((bitField0_ & 0x00000001) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, getPosition());
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(4, getSize());
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof strategies.proto.Chunk)) {
      return super.equals(obj);
    }
    strategies.proto.Chunk other = (strategies.proto.Chunk) obj;

    if (!getId()
        .equals(other.getId())) return false;
    if (!getPlayersList()
        .equals(other.getPlayersList())) return false;
    if (hasPosition() != other.hasPosition()) return false;
    if (hasPosition()) {
      if (!getPosition()
          .equals(other.getPosition())) return false;
    }
    if (hasSize() != other.hasSize()) return false;
    if (hasSize()) {
      if (!getSize()
          .equals(other.getSize())) return false;
    }
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + ID_FIELD_NUMBER;
    hash = (53 * hash) + getId().hashCode();
    if (getPlayersCount() > 0) {
      hash = (37 * hash) + PLAYERS_FIELD_NUMBER;
      hash = (53 * hash) + getPlayersList().hashCode();
    }
    if (hasPosition()) {
      hash = (37 * hash) + POSITION_FIELD_NUMBER;
      hash = (53 * hash) + getPosition().hashCode();
    }
    if (hasSize()) {
      hash = (37 * hash) + SIZE_FIELD_NUMBER;
      hash = (53 * hash) + getSize().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static strategies.proto.Chunk parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static strategies.proto.Chunk parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static strategies.proto.Chunk parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static strategies.proto.Chunk parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static strategies.proto.Chunk parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static strategies.proto.Chunk parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static strategies.proto.Chunk parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static strategies.proto.Chunk parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static strategies.proto.Chunk parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static strategies.proto.Chunk parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static strategies.proto.Chunk parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static strategies.proto.Chunk parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(strategies.proto.Chunk prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code strategies.Chunk}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:strategies.Chunk)
      strategies.proto.ChunkOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return strategies.proto.Data.internal_static_strategies_Chunk_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return strategies.proto.Data.internal_static_strategies_Chunk_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              strategies.proto.Chunk.class, strategies.proto.Chunk.Builder.class);
    }

    // Construct using strategies.proto.Chunk.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
        getPlayersFieldBuilder();
        getPositionFieldBuilder();
        getSizeFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      id_ = "";
      if (playersBuilder_ == null) {
        players_ = java.util.Collections.emptyList();
      } else {
        players_ = null;
        playersBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000002);
      position_ = null;
      if (positionBuilder_ != null) {
        positionBuilder_.dispose();
        positionBuilder_ = null;
      }
      size_ = null;
      if (sizeBuilder_ != null) {
        sizeBuilder_.dispose();
        sizeBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return strategies.proto.Data.internal_static_strategies_Chunk_descriptor;
    }

    @java.lang.Override
    public strategies.proto.Chunk getDefaultInstanceForType() {
      return strategies.proto.Chunk.getDefaultInstance();
    }

    @java.lang.Override
    public strategies.proto.Chunk build() {
      strategies.proto.Chunk result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public strategies.proto.Chunk buildPartial() {
      strategies.proto.Chunk result = new strategies.proto.Chunk(this);
      buildPartialRepeatedFields(result);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartialRepeatedFields(strategies.proto.Chunk result) {
      if (playersBuilder_ == null) {
        if (((bitField0_ & 0x00000002) != 0)) {
          players_ = java.util.Collections.unmodifiableList(players_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.players_ = players_;
      } else {
        result.players_ = playersBuilder_.build();
      }
    }

    private void buildPartial0(strategies.proto.Chunk result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.id_ = id_;
      }
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.position_ = positionBuilder_ == null
            ? position_
            : positionBuilder_.build();
        to_bitField0_ |= 0x00000001;
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
        result.size_ = sizeBuilder_ == null
            ? size_
            : sizeBuilder_.build();
        to_bitField0_ |= 0x00000002;
      }
      result.bitField0_ |= to_bitField0_;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof strategies.proto.Chunk) {
        return mergeFrom((strategies.proto.Chunk)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(strategies.proto.Chunk other) {
      if (other == strategies.proto.Chunk.getDefaultInstance()) return this;
      if (!other.getId().isEmpty()) {
        id_ = other.id_;
        bitField0_ |= 0x00000001;
        onChanged();
      }
      if (playersBuilder_ == null) {
        if (!other.players_.isEmpty()) {
          if (players_.isEmpty()) {
            players_ = other.players_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensurePlayersIsMutable();
            players_.addAll(other.players_);
          }
          onChanged();
        }
      } else {
        if (!other.players_.isEmpty()) {
          if (playersBuilder_.isEmpty()) {
            playersBuilder_.dispose();
            playersBuilder_ = null;
            players_ = other.players_;
            bitField0_ = (bitField0_ & ~0x00000002);
            playersBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getPlayersFieldBuilder() : null;
          } else {
            playersBuilder_.addAllMessages(other.players_);
          }
        }
      }
      if (other.hasPosition()) {
        mergePosition(other.getPosition());
      }
      if (other.hasSize()) {
        mergeSize(other.getSize());
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10: {
              id_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            case 18: {
              strategies.proto.Player m =
                  input.readMessage(
                      strategies.proto.Player.parser(),
                      extensionRegistry);
              if (playersBuilder_ == null) {
                ensurePlayersIsMutable();
                players_.add(m);
              } else {
                playersBuilder_.addMessage(m);
              }
              break;
            } // case 18
            case 26: {
              input.readMessage(
                  getPositionFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000004;
              break;
            } // case 26
            case 34: {
              input.readMessage(
                  getSizeFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000008;
              break;
            } // case 34
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private java.lang.Object id_ = "";
    /**
     * <code>string id = 1;</code>
     * @return The id.
     */
    public java.lang.String getId() {
      java.lang.Object ref = id_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        id_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string id = 1;</code>
     * @return The bytes for id.
     */
    public com.google.protobuf.ByteString
        getIdBytes() {
      java.lang.Object ref = id_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        id_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string id = 1;</code>
     * @param value The id to set.
     * @return This builder for chaining.
     */
    public Builder setId(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      id_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>string id = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearId() {
      id_ = getDefaultInstance().getId();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <code>string id = 1;</code>
     * @param value The bytes for id to set.
     * @return This builder for chaining.
     */
    public Builder setIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      id_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }

    private java.util.List<strategies.proto.Player> players_ =
      java.util.Collections.emptyList();
    private void ensurePlayersIsMutable() {
      if (!((bitField0_ & 0x00000002) != 0)) {
        players_ = new java.util.ArrayList<strategies.proto.Player>(players_);
        bitField0_ |= 0x00000002;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        strategies.proto.Player, strategies.proto.Player.Builder, strategies.proto.PlayerOrBuilder> playersBuilder_;

    /**
     * <code>repeated .strategies.Player players = 2;</code>
     */
    public java.util.List<strategies.proto.Player> getPlayersList() {
      if (playersBuilder_ == null) {
        return java.util.Collections.unmodifiableList(players_);
      } else {
        return playersBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .strategies.Player players = 2;</code>
     */
    public int getPlayersCount() {
      if (playersBuilder_ == null) {
        return players_.size();
      } else {
        return playersBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .strategies.Player players = 2;</code>
     */
    public strategies.proto.Player getPlayers(int index) {
      if (playersBuilder_ == null) {
        return players_.get(index);
      } else {
        return playersBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .strategies.Player players = 2;</code>
     */
    public Builder setPlayers(
        int index, strategies.proto.Player value) {
      if (playersBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensurePlayersIsMutable();
        players_.set(index, value);
        onChanged();
      } else {
        playersBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .strategies.Player players = 2;</code>
     */
    public Builder setPlayers(
        int index, strategies.proto.Player.Builder builderForValue) {
      if (playersBuilder_ == null) {
        ensurePlayersIsMutable();
        players_.set(index, builderForValue.build());
        onChanged();
      } else {
        playersBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .strategies.Player players = 2;</code>
     */
    public Builder addPlayers(strategies.proto.Player value) {
      if (playersBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensurePlayersIsMutable();
        players_.add(value);
        onChanged();
      } else {
        playersBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .strategies.Player players = 2;</code>
     */
    public Builder addPlayers(
        int index, strategies.proto.Player value) {
      if (playersBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensurePlayersIsMutable();
        players_.add(index, value);
        onChanged();
      } else {
        playersBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .strategies.Player players = 2;</code>
     */
    public Builder addPlayers(
        strategies.proto.Player.Builder builderForValue) {
      if (playersBuilder_ == null) {
        ensurePlayersIsMutable();
        players_.add(builderForValue.build());
        onChanged();
      } else {
        playersBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .strategies.Player players = 2;</code>
     */
    public Builder addPlayers(
        int index, strategies.proto.Player.Builder builderForValue) {
      if (playersBuilder_ == null) {
        ensurePlayersIsMutable();
        players_.add(index, builderForValue.build());
        onChanged();
      } else {
        playersBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .strategies.Player players = 2;</code>
     */
    public Builder addAllPlayers(
        java.lang.Iterable<? extends strategies.proto.Player> values) {
      if (playersBuilder_ == null) {
        ensurePlayersIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, players_);
        onChanged();
      } else {
        playersBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .strategies.Player players = 2;</code>
     */
    public Builder clearPlayers() {
      if (playersBuilder_ == null) {
        players_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
      } else {
        playersBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .strategies.Player players = 2;</code>
     */
    public Builder removePlayers(int index) {
      if (playersBuilder_ == null) {
        ensurePlayersIsMutable();
        players_.remove(index);
        onChanged();
      } else {
        playersBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .strategies.Player players = 2;</code>
     */
    public strategies.proto.Player.Builder getPlayersBuilder(
        int index) {
      return getPlayersFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .strategies.Player players = 2;</code>
     */
    public strategies.proto.PlayerOrBuilder getPlayersOrBuilder(
        int index) {
      if (playersBuilder_ == null) {
        return players_.get(index);  } else {
        return playersBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .strategies.Player players = 2;</code>
     */
    public java.util.List<? extends strategies.proto.PlayerOrBuilder> 
         getPlayersOrBuilderList() {
      if (playersBuilder_ != null) {
        return playersBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(players_);
      }
    }
    /**
     * <code>repeated .strategies.Player players = 2;</code>
     */
    public strategies.proto.Player.Builder addPlayersBuilder() {
      return getPlayersFieldBuilder().addBuilder(
          strategies.proto.Player.getDefaultInstance());
    }
    /**
     * <code>repeated .strategies.Player players = 2;</code>
     */
    public strategies.proto.Player.Builder addPlayersBuilder(
        int index) {
      return getPlayersFieldBuilder().addBuilder(
          index, strategies.proto.Player.getDefaultInstance());
    }
    /**
     * <code>repeated .strategies.Player players = 2;</code>
     */
    public java.util.List<strategies.proto.Player.Builder> 
         getPlayersBuilderList() {
      return getPlayersFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        strategies.proto.Player, strategies.proto.Player.Builder, strategies.proto.PlayerOrBuilder> 
        getPlayersFieldBuilder() {
      if (playersBuilder_ == null) {
        playersBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            strategies.proto.Player, strategies.proto.Player.Builder, strategies.proto.PlayerOrBuilder>(
                players_,
                ((bitField0_ & 0x00000002) != 0),
                getParentForChildren(),
                isClean());
        players_ = null;
      }
      return playersBuilder_;
    }

    private strategies.proto.Vector position_;
    private com.google.protobuf.SingleFieldBuilderV3<
        strategies.proto.Vector, strategies.proto.Vector.Builder, strategies.proto.VectorOrBuilder> positionBuilder_;
    /**
     * <code>.strategies.Vector position = 3;</code>
     * @return Whether the position field is set.
     */
    public boolean hasPosition() {
      return ((bitField0_ & 0x00000004) != 0);
    }
    /**
     * <code>.strategies.Vector position = 3;</code>
     * @return The position.
     */
    public strategies.proto.Vector getPosition() {
      if (positionBuilder_ == null) {
        return position_ == null ? strategies.proto.Vector.getDefaultInstance() : position_;
      } else {
        return positionBuilder_.getMessage();
      }
    }
    /**
     * <code>.strategies.Vector position = 3;</code>
     */
    public Builder setPosition(strategies.proto.Vector value) {
      if (positionBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        position_ = value;
      } else {
        positionBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>.strategies.Vector position = 3;</code>
     */
    public Builder setPosition(
        strategies.proto.Vector.Builder builderForValue) {
      if (positionBuilder_ == null) {
        position_ = builderForValue.build();
      } else {
        positionBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>.strategies.Vector position = 3;</code>
     */
    public Builder mergePosition(strategies.proto.Vector value) {
      if (positionBuilder_ == null) {
        if (((bitField0_ & 0x00000004) != 0) &&
          position_ != null &&
          position_ != strategies.proto.Vector.getDefaultInstance()) {
          getPositionBuilder().mergeFrom(value);
        } else {
          position_ = value;
        }
      } else {
        positionBuilder_.mergeFrom(value);
      }
      if (position_ != null) {
        bitField0_ |= 0x00000004;
        onChanged();
      }
      return this;
    }
    /**
     * <code>.strategies.Vector position = 3;</code>
     */
    public Builder clearPosition() {
      bitField0_ = (bitField0_ & ~0x00000004);
      position_ = null;
      if (positionBuilder_ != null) {
        positionBuilder_.dispose();
        positionBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>.strategies.Vector position = 3;</code>
     */
    public strategies.proto.Vector.Builder getPositionBuilder() {
      bitField0_ |= 0x00000004;
      onChanged();
      return getPositionFieldBuilder().getBuilder();
    }
    /**
     * <code>.strategies.Vector position = 3;</code>
     */
    public strategies.proto.VectorOrBuilder getPositionOrBuilder() {
      if (positionBuilder_ != null) {
        return positionBuilder_.getMessageOrBuilder();
      } else {
        return position_ == null ?
            strategies.proto.Vector.getDefaultInstance() : position_;
      }
    }
    /**
     * <code>.strategies.Vector position = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        strategies.proto.Vector, strategies.proto.Vector.Builder, strategies.proto.VectorOrBuilder> 
        getPositionFieldBuilder() {
      if (positionBuilder_ == null) {
        positionBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            strategies.proto.Vector, strategies.proto.Vector.Builder, strategies.proto.VectorOrBuilder>(
                getPosition(),
                getParentForChildren(),
                isClean());
        position_ = null;
      }
      return positionBuilder_;
    }

    private strategies.proto.Vector size_;
    private com.google.protobuf.SingleFieldBuilderV3<
        strategies.proto.Vector, strategies.proto.Vector.Builder, strategies.proto.VectorOrBuilder> sizeBuilder_;
    /**
     * <code>.strategies.Vector size = 4;</code>
     * @return Whether the size field is set.
     */
    public boolean hasSize() {
      return ((bitField0_ & 0x00000008) != 0);
    }
    /**
     * <code>.strategies.Vector size = 4;</code>
     * @return The size.
     */
    public strategies.proto.Vector getSize() {
      if (sizeBuilder_ == null) {
        return size_ == null ? strategies.proto.Vector.getDefaultInstance() : size_;
      } else {
        return sizeBuilder_.getMessage();
      }
    }
    /**
     * <code>.strategies.Vector size = 4;</code>
     */
    public Builder setSize(strategies.proto.Vector value) {
      if (sizeBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        size_ = value;
      } else {
        sizeBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>.strategies.Vector size = 4;</code>
     */
    public Builder setSize(
        strategies.proto.Vector.Builder builderForValue) {
      if (sizeBuilder_ == null) {
        size_ = builderForValue.build();
      } else {
        sizeBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>.strategies.Vector size = 4;</code>
     */
    public Builder mergeSize(strategies.proto.Vector value) {
      if (sizeBuilder_ == null) {
        if (((bitField0_ & 0x00000008) != 0) &&
          size_ != null &&
          size_ != strategies.proto.Vector.getDefaultInstance()) {
          getSizeBuilder().mergeFrom(value);
        } else {
          size_ = value;
        }
      } else {
        sizeBuilder_.mergeFrom(value);
      }
      if (size_ != null) {
        bitField0_ |= 0x00000008;
        onChanged();
      }
      return this;
    }
    /**
     * <code>.strategies.Vector size = 4;</code>
     */
    public Builder clearSize() {
      bitField0_ = (bitField0_ & ~0x00000008);
      size_ = null;
      if (sizeBuilder_ != null) {
        sizeBuilder_.dispose();
        sizeBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>.strategies.Vector size = 4;</code>
     */
    public strategies.proto.Vector.Builder getSizeBuilder() {
      bitField0_ |= 0x00000008;
      onChanged();
      return getSizeFieldBuilder().getBuilder();
    }
    /**
     * <code>.strategies.Vector size = 4;</code>
     */
    public strategies.proto.VectorOrBuilder getSizeOrBuilder() {
      if (sizeBuilder_ != null) {
        return sizeBuilder_.getMessageOrBuilder();
      } else {
        return size_ == null ?
            strategies.proto.Vector.getDefaultInstance() : size_;
      }
    }
    /**
     * <code>.strategies.Vector size = 4;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        strategies.proto.Vector, strategies.proto.Vector.Builder, strategies.proto.VectorOrBuilder> 
        getSizeFieldBuilder() {
      if (sizeBuilder_ == null) {
        sizeBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            strategies.proto.Vector, strategies.proto.Vector.Builder, strategies.proto.VectorOrBuilder>(
                getSize(),
                getParentForChildren(),
                isClean());
        size_ = null;
      }
      return sizeBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:strategies.Chunk)
  }

  // @@protoc_insertion_point(class_scope:strategies.Chunk)
  private static final strategies.proto.Chunk DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new strategies.proto.Chunk();
  }

  public static strategies.proto.Chunk getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Chunk>
      PARSER = new com.google.protobuf.AbstractParser<Chunk>() {
    @java.lang.Override
    public Chunk parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<Chunk> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Chunk> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public strategies.proto.Chunk getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

