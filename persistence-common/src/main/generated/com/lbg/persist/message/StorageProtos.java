// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: messages.proto

package com.lbg.persist.message;

public final class StorageProtos {
  private StorageProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface FreeListRuntimeOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    // required int32 total = 1;
    /**
     * <code>required int32 total = 1;</code>
     */
    boolean hasTotal();
    /**
     * <code>required int32 total = 1;</code>
     */
    int getTotal();

    // required int32 free = 2;
    /**
     * <code>required int32 free = 2;</code>
     */
    boolean hasFree();
    /**
     * <code>required int32 free = 2;</code>
     */
    int getFree();

    // required int32 used = 3;
    /**
     * <code>required int32 used = 3;</code>
     */
    boolean hasUsed();
    /**
     * <code>required int32 used = 3;</code>
     */
    int getUsed();

    // repeated bool available = 4;
    /**
     * <code>repeated bool available = 4;</code>
     */
    java.util.List<java.lang.Boolean> getAvailableList();
    /**
     * <code>repeated bool available = 4;</code>
     */
    int getAvailableCount();
    /**
     * <code>repeated bool available = 4;</code>
     */
    boolean getAvailable(int index);
  }
  /**
   * Protobuf type {@code persist.FreeListRuntime}
   */
  public static final class FreeListRuntime extends
      com.google.protobuf.GeneratedMessage
      implements FreeListRuntimeOrBuilder {
    // Use FreeListRuntime.newBuilder() to construct.
    private FreeListRuntime(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private FreeListRuntime(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final FreeListRuntime defaultInstance;
    public static FreeListRuntime getDefaultInstance() {
      return defaultInstance;
    }

    public FreeListRuntime getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private FreeListRuntime(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              total_ = input.readInt32();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              free_ = input.readInt32();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              used_ = input.readInt32();
              break;
            }
            case 32: {
              if (!((mutable_bitField0_ & 0x00000008) == 0x00000008)) {
                available_ = new java.util.ArrayList<java.lang.Boolean>();
                mutable_bitField0_ |= 0x00000008;
              }
              available_.add(input.readBool());
              break;
            }
            case 34: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              if (!((mutable_bitField0_ & 0x00000008) == 0x00000008) && input.getBytesUntilLimit() > 0) {
                available_ = new java.util.ArrayList<java.lang.Boolean>();
                mutable_bitField0_ |= 0x00000008;
              }
              while (input.getBytesUntilLimit() > 0) {
                available_.add(input.readBool());
              }
              input.popLimit(limit);
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000008) == 0x00000008)) {
          available_ = java.util.Collections.unmodifiableList(available_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.lbg.persist.message.StorageProtos.internal_static_persist_FreeListRuntime_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.lbg.persist.message.StorageProtos.internal_static_persist_FreeListRuntime_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.lbg.persist.message.StorageProtos.FreeListRuntime.class, com.lbg.persist.message.StorageProtos.FreeListRuntime.Builder.class);
    }

    public static com.google.protobuf.Parser<FreeListRuntime> PARSER =
        new com.google.protobuf.AbstractParser<FreeListRuntime>() {
      public FreeListRuntime parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new FreeListRuntime(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<FreeListRuntime> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    // required int32 total = 1;
    public static final int TOTAL_FIELD_NUMBER = 1;
    private int total_;
    /**
     * <code>required int32 total = 1;</code>
     */
    public boolean hasTotal() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required int32 total = 1;</code>
     */
    public int getTotal() {
      return total_;
    }

    // required int32 free = 2;
    public static final int FREE_FIELD_NUMBER = 2;
    private int free_;
    /**
     * <code>required int32 free = 2;</code>
     */
    public boolean hasFree() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>required int32 free = 2;</code>
     */
    public int getFree() {
      return free_;
    }

    // required int32 used = 3;
    public static final int USED_FIELD_NUMBER = 3;
    private int used_;
    /**
     * <code>required int32 used = 3;</code>
     */
    public boolean hasUsed() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>required int32 used = 3;</code>
     */
    public int getUsed() {
      return used_;
    }

    // repeated bool available = 4;
    public static final int AVAILABLE_FIELD_NUMBER = 4;
    private java.util.List<java.lang.Boolean> available_;
    /**
     * <code>repeated bool available = 4;</code>
     */
    public java.util.List<java.lang.Boolean>
        getAvailableList() {
      return available_;
    }
    /**
     * <code>repeated bool available = 4;</code>
     */
    public int getAvailableCount() {
      return available_.size();
    }
    /**
     * <code>repeated bool available = 4;</code>
     */
    public boolean getAvailable(int index) {
      return available_.get(index);
    }

    private void initFields() {
      total_ = 0;
      free_ = 0;
      used_ = 0;
      available_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      if (!hasTotal()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasFree()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasUsed()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeInt32(1, total_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, free_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt32(3, used_);
      }
      for (int i = 0; i < available_.size(); i++) {
        output.writeBool(4, available_.get(i));
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, total_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, free_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, used_);
      }
      {
        int dataSize = 0;
        dataSize = 1 * getAvailableList().size();
        size += dataSize;
        size += 1 * getAvailableList().size();
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.lbg.persist.message.StorageProtos.FreeListRuntime parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.lbg.persist.message.StorageProtos.FreeListRuntime parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.lbg.persist.message.StorageProtos.FreeListRuntime parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.lbg.persist.message.StorageProtos.FreeListRuntime parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.lbg.persist.message.StorageProtos.FreeListRuntime parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.lbg.persist.message.StorageProtos.FreeListRuntime parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.lbg.persist.message.StorageProtos.FreeListRuntime parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.lbg.persist.message.StorageProtos.FreeListRuntime parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.lbg.persist.message.StorageProtos.FreeListRuntime parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.lbg.persist.message.StorageProtos.FreeListRuntime parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.lbg.persist.message.StorageProtos.FreeListRuntime prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code persist.FreeListRuntime}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.lbg.persist.message.StorageProtos.FreeListRuntimeOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.lbg.persist.message.StorageProtos.internal_static_persist_FreeListRuntime_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.lbg.persist.message.StorageProtos.internal_static_persist_FreeListRuntime_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.lbg.persist.message.StorageProtos.FreeListRuntime.class, com.lbg.persist.message.StorageProtos.FreeListRuntime.Builder.class);
      }

      // Construct using com.lbg.persist.message.StorageProtos.FreeListRuntime.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        total_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        free_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        used_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        available_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.lbg.persist.message.StorageProtos.internal_static_persist_FreeListRuntime_descriptor;
      }

      public com.lbg.persist.message.StorageProtos.FreeListRuntime getDefaultInstanceForType() {
        return com.lbg.persist.message.StorageProtos.FreeListRuntime.getDefaultInstance();
      }

      public com.lbg.persist.message.StorageProtos.FreeListRuntime build() {
        com.lbg.persist.message.StorageProtos.FreeListRuntime result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.lbg.persist.message.StorageProtos.FreeListRuntime buildPartial() {
        com.lbg.persist.message.StorageProtos.FreeListRuntime result = new com.lbg.persist.message.StorageProtos.FreeListRuntime(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.total_ = total_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.free_ = free_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.used_ = used_;
        if (((bitField0_ & 0x00000008) == 0x00000008)) {
          available_ = java.util.Collections.unmodifiableList(available_);
          bitField0_ = (bitField0_ & ~0x00000008);
        }
        result.available_ = available_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.lbg.persist.message.StorageProtos.FreeListRuntime) {
          return mergeFrom((com.lbg.persist.message.StorageProtos.FreeListRuntime)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.lbg.persist.message.StorageProtos.FreeListRuntime other) {
        if (other == com.lbg.persist.message.StorageProtos.FreeListRuntime.getDefaultInstance()) return this;
        if (other.hasTotal()) {
          setTotal(other.getTotal());
        }
        if (other.hasFree()) {
          setFree(other.getFree());
        }
        if (other.hasUsed()) {
          setUsed(other.getUsed());
        }
        if (!other.available_.isEmpty()) {
          if (available_.isEmpty()) {
            available_ = other.available_;
            bitField0_ = (bitField0_ & ~0x00000008);
          } else {
            ensureAvailableIsMutable();
            available_.addAll(other.available_);
          }
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasTotal()) {
          
          return false;
        }
        if (!hasFree()) {
          
          return false;
        }
        if (!hasUsed()) {
          
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.lbg.persist.message.StorageProtos.FreeListRuntime parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.lbg.persist.message.StorageProtos.FreeListRuntime) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      // required int32 total = 1;
      private int total_ ;
      /**
       * <code>required int32 total = 1;</code>
       */
      public boolean hasTotal() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required int32 total = 1;</code>
       */
      public int getTotal() {
        return total_;
      }
      /**
       * <code>required int32 total = 1;</code>
       */
      public Builder setTotal(int value) {
        bitField0_ |= 0x00000001;
        total_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required int32 total = 1;</code>
       */
      public Builder clearTotal() {
        bitField0_ = (bitField0_ & ~0x00000001);
        total_ = 0;
        onChanged();
        return this;
      }

      // required int32 free = 2;
      private int free_ ;
      /**
       * <code>required int32 free = 2;</code>
       */
      public boolean hasFree() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>required int32 free = 2;</code>
       */
      public int getFree() {
        return free_;
      }
      /**
       * <code>required int32 free = 2;</code>
       */
      public Builder setFree(int value) {
        bitField0_ |= 0x00000002;
        free_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required int32 free = 2;</code>
       */
      public Builder clearFree() {
        bitField0_ = (bitField0_ & ~0x00000002);
        free_ = 0;
        onChanged();
        return this;
      }

      // required int32 used = 3;
      private int used_ ;
      /**
       * <code>required int32 used = 3;</code>
       */
      public boolean hasUsed() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>required int32 used = 3;</code>
       */
      public int getUsed() {
        return used_;
      }
      /**
       * <code>required int32 used = 3;</code>
       */
      public Builder setUsed(int value) {
        bitField0_ |= 0x00000004;
        used_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required int32 used = 3;</code>
       */
      public Builder clearUsed() {
        bitField0_ = (bitField0_ & ~0x00000004);
        used_ = 0;
        onChanged();
        return this;
      }

      // repeated bool available = 4;
      private java.util.List<java.lang.Boolean> available_ = java.util.Collections.emptyList();
      private void ensureAvailableIsMutable() {
        if (!((bitField0_ & 0x00000008) == 0x00000008)) {
          available_ = new java.util.ArrayList<java.lang.Boolean>(available_);
          bitField0_ |= 0x00000008;
         }
      }
      /**
       * <code>repeated bool available = 4;</code>
       */
      public java.util.List<java.lang.Boolean>
          getAvailableList() {
        return java.util.Collections.unmodifiableList(available_);
      }
      /**
       * <code>repeated bool available = 4;</code>
       */
      public int getAvailableCount() {
        return available_.size();
      }
      /**
       * <code>repeated bool available = 4;</code>
       */
      public boolean getAvailable(int index) {
        return available_.get(index);
      }
      /**
       * <code>repeated bool available = 4;</code>
       */
      public Builder setAvailable(
          int index, boolean value) {
        ensureAvailableIsMutable();
        available_.set(index, value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated bool available = 4;</code>
       */
      public Builder addAvailable(boolean value) {
        ensureAvailableIsMutable();
        available_.add(value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated bool available = 4;</code>
       */
      public Builder addAllAvailable(
          java.lang.Iterable<? extends java.lang.Boolean> values) {
        ensureAvailableIsMutable();
        super.addAll(values, available_);
        onChanged();
        return this;
      }
      /**
       * <code>repeated bool available = 4;</code>
       */
      public Builder clearAvailable() {
        available_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000008);
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:persist.FreeListRuntime)
    }

    static {
      defaultInstance = new FreeListRuntime(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:persist.FreeListRuntime)
  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_persist_FreeListRuntime_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_persist_FreeListRuntime_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\016messages.proto\022\007persist\"O\n\017FreeListRun" +
      "time\022\r\n\005total\030\001 \002(\005\022\014\n\004free\030\002 \002(\005\022\014\n\004use" +
      "d\030\003 \002(\005\022\021\n\tavailable\030\004 \003(\010B(\n\027com.lbg.pe" +
      "rsist.messageB\rStorageProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_persist_FreeListRuntime_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_persist_FreeListRuntime_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_persist_FreeListRuntime_descriptor,
              new java.lang.String[] { "Total", "Free", "Used", "Available", });
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}
